package com.kretov.accountmanagement.view;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DepositEditor extends VerticalLayout implements KeyNotifier {
    private final AccountService accountService;

    private Account account;

    private TextField money = new TextField("Money");

    private Button deposit = new Button("Deposit", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(deposit);

    private ChangeHandler changeHandler;

    public DepositEditor(AccountService accountService) {
        this.accountService = accountService;

        money.setPlaceholder("Only positive numbers");

        add(money, actions);

        setSpacing(true);

        deposit.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> deposit(money.getValue()));

        deposit.addClickListener(e -> deposit(money.getValue()));
        setVisible(false);
    }

    private void deposit(String money) {
        try {
            Double depositMoney = Double.valueOf(money);
            if (depositMoney > 0) {
                accountService.depositMoney(account, depositMoney);
                changeHandler.onChange();
            }
        } catch (NumberFormatException e) {
            changeHandler.onChange();
        }
    }

    public interface ChangeHandler {
        void onChange();
    }

    final void editAccount(Account editableAccount) {
        if (editableAccount == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = editableAccount.getId() != null;
        if (persisted) {
            account = accountService.findById(editableAccount.getId());
        } else {
            account = editableAccount;
        }

        setVisible(true);
        money.focus();
    }

    void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

}
