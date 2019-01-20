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

import static com.kretov.accountmanagement.view.Util.showNotification;

@SpringComponent
@UIScope
public class TransferEditor extends VerticalLayout implements KeyNotifier {
    private final AccountService accountService;

    private Account account;

    private TextField money = new TextField("Money");
    private TextField destinationAccountId = new TextField("Destination account id");

    private Button transferBtn = new Button("Transfer", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(transferBtn);

    private ChangeHandler changeHandler;

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
            if (transferMoney > 0) {
                boolean status = accountService.transferMoney(account, accountService.findById(destinationAccountId), transferMoney);
                if (!status) {
                    showNotification("Transfer failed. Not enough money");
                }
                changeHandler.onChange();
            } else {
                showNotification("Money must be a positive number");
            }
        } catch (NumberFormatException e) {
            showNotification("Illegal input format");
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
