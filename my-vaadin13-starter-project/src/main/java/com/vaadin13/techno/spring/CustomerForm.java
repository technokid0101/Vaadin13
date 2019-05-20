package com.vaadin13.techno.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.DAO.Customer;
import com.DAO.CustomerDAO;
import com.DAO.CustomerDAOImpl;
import com.DAO.CustomerStatus;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomerForm extends SplitLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected TextField firstName, lastName, filterText;
	protected DatePicker birthDate;
	protected HorizontalLayout horizontalLayout;
	protected Button buttonSave, buttonDelete, buttonAddCustomer, buttonUpdate;
	protected Grid<Customer> grid = new Grid<Customer>(Customer.class);
	protected VerticalLayout verticalLayout = new VerticalLayout();
	protected ComboBox<CustomerStatus> status;
	private static CustomerDAO customerDAO = CustomerDAOImpl.getInstance();
	private Binder<Customer> binder = new Binder<Customer>(Customer.class);
	private ProgressBar bar = new ProgressBar();
	final String[] names = new String[] { "Gabrielle Patel", "Brian Robinson", "Eduardo Haugen", "Koen Johansen",
			"Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson", "Emily Stewart",
			"Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson", "Eileen Walker", "Katelyn Martin",
			"Israel Carlsson", "Quinn Hansson", "Makena Smith", "Danielle Watson", "Leland Harris", "Gunner Karlsen",
			"Jamar Olsson", "Lara Martin", "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen",
			"Solomon Olsen", "Jaydan Jackson", "Bernard Nilsen" };

	public CustomerForm() {
		bar.setIndeterminate(true);
		bar.setVisible(false);
		firstName = new TextField();
		firstName.setPlaceholder("First Name");
		firstName.setClearButtonVisible(true);
		firstName.setSizeFull();
		lastName = new TextField();
		lastName.setPlaceholder("Last Name");
		lastName.setClearButtonVisible(true);
		lastName.setSizeFull();
		status = new ComboBox<>("Status");
		status.setItems(CustomerStatus.values());
		status.setSizeFull();
		filterText = new TextField();
		filterText.setPlaceholder("Filter By Text..");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.EAGER);
		filterText.addValueChangeListener(e -> updateList(filterText.getValue()));
		birthDate = new DatePicker();
		birthDate.setPlaceholder("Birth Date");
		birthDate.setSizeFull();
		horizontalLayout = new HorizontalLayout();
		buttonSave = new Button("Save");
		buttonDelete = new Button("Delete");
		buttonAddCustomer = new Button("Add Customer");
		buttonUpdate = new Button("Update");
		verticalLayout.add(firstName, lastName, birthDate, status, horizontalLayout, bar);
		horizontalLayout.add(buttonSave, buttonUpdate, buttonDelete);
		buttonUpdate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		buttonAddCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addToSecondary(verticalLayout);
		verticalLayout = new VerticalLayout();
		horizontalLayout = new HorizontalLayout();
		horizontalLayout.add(filterText, buttonAddCustomer);
		grid.setColumns("firstName", "lastName", "status");
		updateList();
		verticalLayout.add(horizontalLayout, grid);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.asSingleSelect().addValueChangeListener(event -> setCustomer(grid.asSingleSelect().getValue()));
		binder.bindInstanceFields(this);
		addToPrimary(verticalLayout);
		setCustomer(null);
		buttonSave.addClickListener(evt -> {
			save();
		});
		buttonDelete.addClickListener(evt -> {
			delete();
		});
		buttonAddCustomer.addClickListener(evt -> {
			grid.asSingleSelect().clear();
			setCustomer(new Customer());
		});
		buttonUpdate.addClickListener(evt -> {
			update();
		});
		setSizeFull();
	}

	public void setCustomer(Customer customer) {
		binder.setBean(customer);
		if (customer == null) {
		} else {
			setVisible(true);
			firstName.focus();
		}
	}

	private void save() {
		Random r = new Random();
		Random rId = new Random();
		int id = rId.nextInt();
		if (id != 0) {
			Customer customer = new Customer();
			customer.setId((long) rId.nextInt());
			customer.setFirstName(firstName.getValue());
			customer.setLastName(lastName.getValue());
			customer.setBirthDate(birthDate.getValue());
			customer.setStatus(status.getValue());
			int randomIndex = r.nextInt(names.length);
			String name = names[randomIndex];
			String[] split = name.split(" ");
			String email = split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com";
			customer.setEmail(email);
			UI ui = UI.getCurrent();
			bar.setVisible(true);
			ui.setPollInterval(100);
			CompletableFuture.runAsync(() -> {
				customerDAO.add(customer);
				ui.access(() -> {
					ui.setPollInterval(-1);
					bar.setVisible(false);
					Notification notification = new Notification("Sucess..!!", 3000, Position.MIDDLE);
					notification.setText("Cutomer " + customer.getFirstName() + " added sucessfully..!!");
					notification.open();
					updateList();
				});
			});
			setCustomer(null);
		} else {
			save();
		}
	}

	private void delete() {
		Customer customer = binder.getBean();
		UI ui = UI.getCurrent();
		bar.setVisible(true);
		ui.setPollInterval(100);
		CompletableFuture.runAsync(() -> {
			customerDAO.delete(customer.getId());
			ui.access(() -> {
				ui.setPollInterval(-1);
				bar.setVisible(false);
				Notification notification = new Notification("Sucess..!!", 3000, Position.MIDDLE);
				notification.setText("Cutomer " + customer.getFirstName() + " deleted sucessfully..!!");
				notification.open();
				updateList();
			});
		});
		setCustomer(null);

	}

	public void updateList() {
		grid.setItems(findAll(null));
	}

	public void updateList(String text) {
		grid.setItems(findAll(text));
	}

	public void update() {
		Customer customer = binder.getBean();
		UI ui = UI.getCurrent();
		bar.setVisible(true);
		ui.setPollInterval(100);
		CompletableFuture.runAsync(() -> {
			customerDAO.update(customer);
			ui.access(() -> {
				ui.setPollInterval(-1);
				bar.setVisible(false);
				Notification notification = new Notification("Sucess..!!", 3000, Position.MIDDLE);
				notification.setText("Cutomer " + customer.getFirstName() + " updated sucessfully..!!");
				notification.open();
				updateList();
			});
		});
		setCustomer(null);
	}

	public synchronized List<Customer> findAll(String stringFilter) {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : customerDAO.findAll()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(this.getClassName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Customer>() {

			@Override
			public int compare(Customer o1, Customer o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}
}
