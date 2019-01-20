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
public class TransferEditor extends AbstractAccountEditor {
    private final AccountService accountService;

    private TextField money = new TextField("Money");
    private TextField destinationAccountId = new TextField("Destination account id");

    private Button transferBtn = new Button("Transfer", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(transferBtn);

    public TransferEditor(AccountService accountService) {
        this.accountService = accountService;

        money.setPlaceholder("Only positive numbers");

        add(money, destinationAccountId, actions);

        setSpacing(true);

        transferBtn.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> transfer(money.getValue(), destinationAccountId.getValue()));

        transferBtn.addClickListener(e -> transfer(money.getValue(), destinationAccountId.getValue()));
        setVisible(false);
    }

    private void transfer(String money, String destination) {
        try {
            Double transferMoney = Double.valueOf(money);
            Long destinationAccountId = Long.valueOf(destination);
            if (destinationAccountId.equals(account.getId())) {
                showNotification("Illegal format of input data. Please, use different account ids.");
            } else {
                if (transferMoney > 0) {
                    boolean status = accountService.transferMoney(account, accountService.findById(destinationAccountId), transferMoney);
                    if (!status) {
                        showNotification("Transfer failed. Not enough money");
                    }
                    getChangeHandler().onChange();
                } else {
                    showNotification("Money must be a positive number");
                }
            }
        } catch (NumberFormatException e) {
            showNotification("Illegal input format");
        }
    }

    public final void editAccount(Account editableAccount) {
        processAccount(editableAccount, accountService);
    }

}
