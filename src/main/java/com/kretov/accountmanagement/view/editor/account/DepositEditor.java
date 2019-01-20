package com.kretov.accountmanagement.view.editor.account;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import static com.kretov.accountmanagement.view.util.Util.showNotification;

@SpringComponent
@UIScope
public class DepositEditor extends AbstractAccountEditor {
    private final AccountService accountService;

    private Button depositBtn = new Button("Deposit", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(depositBtn);

    public DepositEditor(AccountService accountService) {
        this.accountService = accountService;

        money.setPlaceholder("Only positive numbers");

        add(money, actions);

        setSpacing(true);

        depositBtn.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> deposit(money.getValue()));

        depositBtn.addClickListener(e -> deposit(money.getValue()));
        setVisible(false);
    }

    private void deposit(String money) {
        try {
            Double depositMoney = Double.valueOf(money);
            if (depositMoney > 0) {
                accountService.depositMoney(account, depositMoney);
                getChangeHandler().onChange();
            } else {
                showNotification("Money must be a positive number");
            }
        } catch (NumberFormatException e) {
            showNotification("Illegal input format");
        }
    }

    public final void editAccount(Account editableAccount) {
        processAccount(editableAccount, accountService);
    }

}
