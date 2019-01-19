package com.kretov.accountmanagement.view;

import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Route(value = "ui")
public class MainView extends VerticalLayout {

    private CustomerService customerService;
    private CustomerEditor customerEditor;

    private Grid<Customer> customerGrid;
    private TextField customerFilter;
    private Button addNewCustomerBtn;

    public MainView(CustomerService customerService, CustomerEditor editor) {
        this.customerService = customerService;
        this.customerEditor = editor;
        this.customerGrid = new Grid<>(Customer.class);
        this.customerFilter = new TextField();
        this.addNewCustomerBtn = new Button("Add customer", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(customerFilter, addNewCustomerBtn);
        add(actions, customerGrid, customerEditor);

        customerGrid.setHeight("200px");
        customerGrid.setColumns("id", "firstName", "lastName");
        customerGrid.getColumnByKey("id").setWidth("100px").setFlexGrow(0);

        customerFilter.setPlaceholder("Filter by last name");

        customerFilter.setValueChangeMode(ValueChangeMode.EAGER);
        customerFilter.addValueChangeListener(e -> customerList(e.getValue()));

        customerGrid.asSingleSelect().addValueChangeListener(e -> customerEditor.editCustomer(e.getValue()));

        addNewCustomerBtn.addClickListener(e -> customerEditor.editCustomer(new Customer("", "")));

        customerEditor.setChangeHandler(() -> {
            customerEditor.setVisible(false);
            customerList(customerFilter.getValue());
        });

        customerList(null);
    }

    private void customerList(String lastName) {
        if (isBlank(lastName)) {
            customerGrid.setItems(customerService.findAll());
        } else {
            customerGrid.setItems(customerService.findByLastName(lastName));
        }
    }
}
