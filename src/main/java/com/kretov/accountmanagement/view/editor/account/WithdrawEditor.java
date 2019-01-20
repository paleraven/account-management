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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import static com.kretov.accountmanagement.view.util.Util.showNotification;

@SpringComponent
@UIScope
public class WithdrawEditor extends AbstractAccountEditor {
    private final AccountController accountController;

    private TextField money = new TextField("Money");

    private Button withdrawBtn = new Button("Withdraw", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(withdrawBtn);

    public WithdrawEditor(AccountController accountController) {
        this.accountController = accountController;

        money.setPlaceholder("Only positive numbers");

        add(money, actions);

        setSpacing(true);

        withdrawBtn.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> withdraw(money.getValue()));

        withdrawBtn.addClickListener(e -> withdraw(money.getValue()));
        setVisible(false);
    }

    private void withdraw(String money) {
        Response<Account> response = accountController.withdrawMoney(account.getId().toString(), money);
        if(response.getStatus().equals(Status.ERROR)) {
            showNotification(response.getDescription());
        } else {
            getChangeHandler().onChange();
        }
    }

    public final void editAccount(Account editableAccount) {
        processAccount(editableAccount, accountController);
    }

}
