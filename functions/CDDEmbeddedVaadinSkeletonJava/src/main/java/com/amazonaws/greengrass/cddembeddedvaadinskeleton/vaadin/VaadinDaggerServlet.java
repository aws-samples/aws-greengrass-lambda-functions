package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class VaadinDaggerServlet extends VaadinServlet {
    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService service = createVaadinServletService(deploymentConfiguration);
        service.init();
        return service;
    }

    protected VaadinServletService createVaadinServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService vaadinServletService = new VaadinDaggerServletService(this, deploymentConfiguration);

        return vaadinServletService;
    }
}
