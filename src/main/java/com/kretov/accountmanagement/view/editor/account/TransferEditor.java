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
public class TransferEditor extends AbstractAccountEditor {
    private final AccountController accountController;

    private TextField money = new TextField("Money");
    private TextField destinationAccountId = new TextField("Destination account id");

    private Button transferBtn = new Button("Transfer", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(transferBtn);

    public TransferEditor(AccountController accountController) {
        this.accountController = accountController;

        money.setPlaceholder("Only positive numbers");

        add(money, destinationAccountId, actions);

        setSpacing(true);

        transferBtn.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> transfer(money.getValue(), destinationAccountId.getValue()));

        transferBtn.addClickListener(e -> transfer(money.getValue(), destinationAccountId.getValue()));
        setVisible(false);
    }

    private void transfer(String money, String destination) {
        Response<Account> response = accountController.transferMoney(account.getId().toString(), destination, money);
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
