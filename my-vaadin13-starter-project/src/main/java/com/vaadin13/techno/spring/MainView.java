package com.vaadin13.techno.spring;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CustomerForm customerForm = new CustomerForm();

//	public MainView(@Autowired MessageBean bean) {
//		add(customerForm);
//	}

	public MainView() {
		add(customerForm);
		setSizeFull();
	}

}
