package com.kretov.accountmanagement.view;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
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

    private AccountService accountService;
    private AccountEditor accountEditor;

    private Grid<Customer> customerGrid;
    private TextField customerFilter;
    private Button addNewCustomerBtn;
    private Label customerBlockTitle;

    private Grid<Account> accountGrid;
    private TextField accountFilter;
    private Button addNewAccountBtn;
    private Label accountBlockTitle;

    public MainView(CustomerService customerService, CustomerEditor editor, AccountService accountService, AccountEditor accEditor) {
        this.customerService = customerService;
        this.customerEditor = editor;
        this.accountService = accountService;
        this.accountEditor = accEditor;

        this.customerGrid = new Grid<>(Customer.class);
        this.customerFilter = new TextField();
        this.addNewCustomerBtn = new Button("Add customer", VaadinIcon.PLUS.create());
        this.customerBlockTitle = new Label("Customers");

        this.accountGrid = new Grid<>(Account.class);
        this.accountFilter = new TextField();
        this.addNewAccountBtn = new Button("Add account", VaadinIcon.PLUS.create());
        this.accountBlockTitle = new Label("Accounts");

        //Customers
        HorizontalLayout customerBlock = new HorizontalLayout(customerBlockTitle);
        HorizontalLayout actions = new HorizontalLayout(customerFilter, addNewCustomerBtn);
        add(customerBlock, actions, customerGrid, customerEditor);

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
            accountList(accountFilter.getValue());
        });

        customerList(null);

        //Accounts
        HorizontalLayout accountBlock = new HorizontalLayout(accountBlockTitle);
        HorizontalLayout accountActions = new HorizontalLayout(accountFilter, addNewAccountBtn);
        add(accountBlock, accountActions, accountGrid, accountEditor);

        accountGrid.setHeight("300px");
        accountGrid.setColumns("id", "customer", "money");
        accountGrid.getColumnByKey("id").setWidth("100px").setFlexGrow(0);

        accountFilter.setPlaceholder("Filter by customer id");

        accountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        accountFilter.addValueChangeListener(e -> accountList(e.getValue()));

        accountGrid.asSingleSelect().addValueChangeListener(e -> accountEditor.editAccount(e.getValue()));

        addNewAccountBtn.addClickListener(e -> accountEditor.editAccount(new Account(customerService.findAll().get(0), null)));

        accountEditor.setChangeHandler(() -> {
            accountEditor.setVisible(false);
            accountList(accountFilter.getValue());
        });

        accountList(null);
    }

    private void customerList(String lastName) {
        if (isBlank(lastName)) {
            customerGrid.setItems(customerService.findAll());
        } else {
            customerGrid.setItems(customerService.findByLastName(lastName));
        }
    }

    private void accountList(String id) {
        if (isBlank(id)) {
            accountGrid.setItems(accountService.findAllAccounts());
        } else {
            accountGrid.setItems(accountService.findByCustomer(customerService.findById(Long.valueOf(id))));
        }
    }
}
