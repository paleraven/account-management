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

    private DepositEditor depositEditor;
    private WithdrawEditor withdrawEditor;
    private TransferEditor transferEditor;

    private Grid<Customer> customerGrid;
    private TextField customerFilter;
    private Button addNewCustomerBtn;
    private Label customerBlockTitle;

    private Grid<Account> accountGrid;
    private Button addNewAccountBtn;
    private Button depositMoneyBtn;
    private Button withdrawMoneyBtn;
    private Button transferMoneyBtn;
    private Label accountBlockTitle;

    public MainView(CustomerService customerService, CustomerEditor editor,
                    AccountService accountService, AccountEditor accEditor,
                    DepositEditor depEditor, WithdrawEditor withdrawEdit, TransferEditor transferEdit) {
        this.customerService = customerService;
        this.customerEditor = editor;
        this.accountService = accountService;
        this.accountEditor = accEditor;
        this.depositEditor = depEditor;
        this.withdrawEditor = withdrawEdit;
        this.transferEditor = transferEdit;

        this.customerGrid = new Grid<>(Customer.class);
        this.customerFilter = new TextField();
        this.addNewCustomerBtn = new Button("Add customer", VaadinIcon.PLUS.create());
        this.customerBlockTitle = new Label("Customers");

        this.accountGrid = new Grid<>(Account.class);
        accountGrid.onEnabledStateChanged(false);

        //Account buttons
        this.addNewAccountBtn = new Button("Add account", VaadinIcon.PLUS.create());
        addNewAccountBtn.onEnabledStateChanged(false);
        this.depositMoneyBtn = new Button("Deposit", VaadinIcon.WALLET.create());
        depositMoneyBtn.onEnabledStateChanged(false);
        this.withdrawMoneyBtn = new Button("Withdraw", VaadinIcon.CASH.create());
        withdrawMoneyBtn.onEnabledStateChanged(false);
        this.transferMoneyBtn = new Button("Transfer", VaadinIcon.ARROW_RIGHT.create());
        transferMoneyBtn.onEnabledStateChanged(false);

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

        customerGrid.asSingleSelect().addValueChangeListener(e -> {
            customerEditor.editCustomer(e.getValue());
            accountList(e.getValue() != null ? e.getValue().getId().toString() : null);
            accountGrid.onEnabledStateChanged(true);
            addNewAccountBtn.onEnabledStateChanged(true);
            depositMoneyBtn.onEnabledStateChanged(true);
            withdrawMoneyBtn.onEnabledStateChanged(true);
            transferMoneyBtn.onEnabledStateChanged(true);
            accountEditor.setVisible(false);
        });

        addNewCustomerBtn.addClickListener(e -> customerEditor.editCustomer(new Customer("", "")));

        customerEditor.setChangeHandler(() -> {
            customerEditor.setVisible(false);
            customerList(customerFilter.getValue());
        });

        customerList(null);

        //Accounts
        HorizontalLayout accountBlock = new HorizontalLayout(accountBlockTitle);
        HorizontalLayout accountActions = new HorizontalLayout(addNewAccountBtn, depositMoneyBtn, withdrawMoneyBtn, transferMoneyBtn);
        add(accountBlock, accountActions, accountGrid, accountEditor, depositEditor, withdrawEditor, transferEditor);

        accountGrid.setHeight("300px");
        accountGrid.setColumns("id", "money");
        accountGrid.getColumnByKey("id").setWidth("100px").setFlexGrow(0);

        accountGrid.asSingleSelect().addValueChangeListener(e -> accountEditor.editAccount(e.getValue()));

        //account operations listeners
        addNewAccountBtn.addClickListener(e -> {
            String customerId = customerGrid.asSingleSelect().getValue().getId().toString();
            accountEditor.editAccount(new Account(customerService.findById(Long.valueOf(customerId)), null));
            accountList(customerId);
        });

        depositMoneyBtn.addClickListener(e -> {
            String customerId = customerGrid.asSingleSelect().getValue().getId().toString();
            depositEditor.editAccount(accountGrid.asSingleSelect().getValue());
            accountList(customerId);
        });

        withdrawMoneyBtn.addClickListener(e -> {
            String customerId = customerGrid.asSingleSelect().getValue().getId().toString();
            withdrawEditor.editAccount(accountGrid.asSingleSelect().getValue());
            accountList(customerId);
        });

        transferMoneyBtn.addClickListener(e -> {
            String customerId = customerGrid.asSingleSelect().getValue().getId().toString();
            transferEditor.editAccount(accountGrid.asSingleSelect().getValue());
            accountList(customerId);
        });

        //account handlers
        accountEditor.setChangeHandler(() -> {
            accountEditor.setVisible(false);
            accountList(customerGrid.asSingleSelect().getValue().getId().toString());
        });

        depositEditor.setChangeHandler(() -> {
            depositEditor.setVisible(false);
            accountList(customerGrid.asSingleSelect().getValue().getId().toString());
        });

        withdrawEditor.setChangeHandler(() -> {
            withdrawEditor.setVisible(false);
            accountList(customerGrid.asSingleSelect().getValue().getId().toString());
        });

        transferEditor.setChangeHandler(() -> {
            transferEditor.setVisible(false);
            accountList(customerGrid.asSingleSelect().getValue().getId().toString());
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
