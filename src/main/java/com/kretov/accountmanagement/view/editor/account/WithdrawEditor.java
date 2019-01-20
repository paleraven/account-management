package com.kretov.accountmanagement.view.editor.account;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
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
    private final AccountService accountService;

    private TextField money = new TextField("Money");

    private Button withdrawBtn = new Button("Withdraw", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(withdrawBtn);

    public WithdrawEditor(AccountService accountService) {
        this.accountService = accountService;

        money.setPlaceholder("Only positive numbers");

        add(money, actions);

        setSpacing(true);

        withdrawBtn.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> withdraw(money.getValue()));

        withdrawBtn.addClickListener(e -> withdraw(money.getValue()));
        setVisible(false);
    }

    private void withdraw(String money) {
        try {
            Double depositMoney = Double.valueOf(money);
            if (depositMoney > 0) {
                boolean status = accountService.withdrawMoney(account, depositMoney);
                if (!status) {
                    showNotification("Withdraw failed. Not enough money");
                }
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
