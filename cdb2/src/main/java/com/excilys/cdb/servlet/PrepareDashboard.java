package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.servlet.Order.OrderBy;
import com.excilys.cdb.util.PageManager;

@Controller
public class PrepareDashboard {
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "10";
    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareDashboard.class);

    @RequestMapping("/dashboard")
    public ModelAndView treatRequest(String search,
            @RequestParam(defaultValue = DEFAULT_OFFSET) String offset,
            @RequestParam(defaultValue = DEFAULT_LIMIT) String limit,
            String orderby, String order) {
        LOGGER.debug("PrepareDashboard : entered in treatRequest");
//        ServiceComputer serviceComputer =
//                ServletCommonFunction.getComputerService();
//        String offset = getOffset(request);
//        String limit = getLimit(request);
//        Optional<String> search =
//                ServletCommonFunction.getParameter(request, "search");
        Optional<Order> oOrder = getOrder(orderby, order);
        try {
            return prepareAndForward(offset, limit, search, oOrder);
        } catch (Exception e) {
            return ServletCommonFunction.dealWithException(e);
        }
    }

//    private String verifOffset(String offset) {
//        if (offset == null || "".contentEquals(offset)) {
//            return DEFAULT_OFFSET;
//        } else {
//            return offset;
//        }
//    }
//
//    private String verifLimit(String limit) {
//        if (limit == null || "".contentEquals(limit)) {
//            return DEFAULT_LIMIT;
//        } else {
//            return limit;
//        }
//    }

    private long getNbComputers(ServiceComputer serviceComputer)
            throws DBException {
        return serviceComputer.countComputers();
    }

    private Optional<PageManager> buildPageManager(String offset, String limit,
            long nbComputers) {
        try {
            return Optional.of(PageManager.builder().withNbItem(nbComputers)
                    .withLimit(Mapper.mapLong(limit))
                    .withOffset(Mapper.mapLong(offset)).build());
        } catch (NotLongException e) {
            return Optional.empty();
        }
    }

    private ModelAndView prepareAndForward(String offset, String limit,
            String search, Optional<Order> order)
            throws ServletException, IOException {
        try {
            return prepare(offset, limit, search, order);
        } catch (Exception e) {
            return ServletCommonFunction.dealWithException(e);
        }
    }

    private ModelAndView prepare(String offset, String limit, String search,
            Optional<Order> order)
            throws InvalidPageException, ProblemListException, DBException {
        ServiceComputer serviceComputer =
                ServletCommonFunction.getComputerService();
//        offset = verifOffset(offset);
//        limit = verifLimit(limit);
        LOGGER.debug("offset = " + offset + "; limit = " + limit);
        PageDTO pageDTO =
                PageDTO.builder().withOffset(offset).withLimit(limit).build();
        ModelAndView modelAndView =
                ServletCommonFunction.forward(Address.DASHBOARD);
        List<ComputerDTO> listComputer;
        long nbComputers;
        if (!(search == null || "".contentEquals(search))) {
            if (order.isPresent()) {
                listComputer = serviceComputer.getComputers(pageDTO, search,
                        order.get());
            } else {
                listComputer = serviceComputer.getComputers(pageDTO, search);
            }
            modelAndView.addObject("search", search);
            nbComputers = getNbComputers(serviceComputer, search);
        } else {
            if (order.isPresent()) {
                listComputer =
                        serviceComputer.getComputers(pageDTO, order.get());
            } else {
                listComputer = serviceComputer.getComputers(pageDTO);
            }
            nbComputers = getNbComputers(serviceComputer);
        }
        modelAndView.addObject("list", listComputer);
        setOrder(modelAndView, order);
        PageManager pageManager = buildPageManager(offset, limit, nbComputers)
                .orElseThrow(AbsurdOptionalException::new);
        return modelAndView.addObject("pagemanager", pageManager);
    }

    private void setOrder(ModelAndView modelAndView, Optional<Order> order) {
        if (order.isEmpty()) {
            modelAndView.addObject("orderby", "");
            modelAndView.addObject("order", "");
        } else {
            if (order.get().isAsc()) {
                modelAndView.addObject("order", "asc");
            } else {
                modelAndView.addObject("order", "desc");
            }
            switch (order.get().getOrderBy()) {
            case COMPANY:
                modelAndView.addObject("orderby", "company");
                break;
            case COMPUTER:
                modelAndView.addObject("orderby", "computer");
                break;
            case DISCONTINUED:
                modelAndView.addObject("orderby", "discontinued");
                break;
            case INTRODUCED:
                modelAndView.addObject("orderby", "introduced");
                break;
            default:
                throw new RuntimeException(
                        "You forgot to complete the switch in PrepareDashboard");
            }
        }
    }

    private long getNbComputers(ServiceComputer serviceComputer, String search)
            throws DBException {
        return serviceComputer.countComputers(search);
    }

    private Optional<Order> getOrder(String orderby, String order) {
        if (orderby == null || order == null || "".equals(order)
                || "".equals(orderby)) {
            LOGGER.debug("No order provided to dashboard");
            return Optional.empty();
        }
        OrderBy orderBy;
        switch (orderby) {
        case "computer":
            orderBy = OrderBy.COMPUTER;
            break;
        case "introduced":
            orderBy = OrderBy.INTRODUCED;
            break;
        case "discontinued":
            orderBy = OrderBy.DISCONTINUED;
            break;
        case "company":
            orderBy = OrderBy.COMPANY;
            break;
        default:
            LOGGER.debug("Orderby was found but contains an incorrect value");
            return Optional.empty();
        }
        boolean asc;
        switch (order) {
        case "asc":
            asc = true;
            break;
        case "desc":
            asc = false;
            break;
        default:
            LOGGER.debug("order was found but contains an incorrect value");
            return Optional.empty();
        }
        return Optional
                .of(Order.builder().withOrderBy(orderBy).withAsc(asc).build());
    }
}
