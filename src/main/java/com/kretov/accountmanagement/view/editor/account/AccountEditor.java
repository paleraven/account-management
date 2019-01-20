package com.kretov.accountmanagement.view.editor.account;

import com.kretov.accountmanagement.controller.AccountController;
import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.response.Response;
import com.kretov.accountmanagement.response.Status;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import static com.kretov.accountmanagement.view.util.Util.showNotification;

@SpringComponent
@UIScope
public class AccountEditor extends AbstractAccountEditor {
    private final AccountController accountController;

    private TextField customerId = new TextField("Customer id");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Account> binder = new Binder<>(Account.class);

    public AccountEditor(AccountController accountController) {
        this.accountController = accountController;

        add(customerId, money, actions);

        binder.forField(money)
                .withNullRepresentation("")
                .withConverter(
                        new StringToDoubleConverter( Double.valueOf(0), "numbers only" ) )
                .bind(Account::getMoney, Account::setMoney);
        binder.forField(customerId)
                .withConverter(new StringToLongConverter("error message"))
                .bind(item -> item.getCustomer().getId(), (item, value) -> item.getCustomer().setId(value));
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> insert());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editAccount(account));
        setVisible(false);
    }

    private void delete() {
        accountController.deleteAccountByAccountId(account.getId().toString());
        getChangeHandler().onChange();
    }

    private void save() {
        Response<Account> response = accountController.updateAccount(account);
        if (response.getStatus().equals(Status.ERROR)) {
            showNotification(response.getDescription());
        } else {
            getChangeHandler().onChange();
        }
    }

    private void insert() {
        Response<Account> response = accountController.createAccount(account);
        if (response.getStatus().equals(Status.ERROR)) {
            showNotification(response.getDescription());
        } else {
            getChangeHandler().onChange();
        }
    }

    public final void editAccount(Account editableAccount) {
        if (editableAccount == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = editableAccount.getId() != null;
        if (persisted) {
            account = accountController.getAccountByAccountId(editableAccount.getId().toString()).getResult().get(0);
        } else {
            account = editableAccount;
        }

        cancel.setVisible(persisted);
        binder.setBean(account);
        setVisible(true);
        customerId.focus();
    }
}
