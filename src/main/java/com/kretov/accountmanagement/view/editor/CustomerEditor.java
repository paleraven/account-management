package com.kretov.accountmanagement.view.editor;

import com.kretov.accountmanagement.controller.CustomerController;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.CustomerService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class CustomerEditor extends AbstractEditor {

    private final CustomerController customerController;

    private Customer customer;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Customer> binder = new Binder<>(Customer.class);

    @Autowired
    public CustomerEditor(CustomerController controller) {
        this.customerController = controller;

        add(firstName, lastName, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> insert());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    private void delete() {
        customerController.deleteCustomerByCustomerId(customer.getId().toString());
        getChangeHandler().onChange();
    }

    private void insert() {
        customerController.createCustomer(customer);
        getChangeHandler().onChange();
    }

    private void save() {
        customerController.updateCustomer(customer);
        getChangeHandler().onChange();
    }

    public final void editCustomer(Customer editableCustomer) {
        if (editableCustomer == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = editableCustomer.getId() != null;
        if (persisted) {
            customer = customerController.findById(editableCustomer.getId().toString()).getResult().get(0);
        } else {
            customer = editableCustomer;
        }

        cancel.setVisible(persisted);
        binder.setBean(customer);
        setVisible(true);
        firstName.focus();
    }
}
