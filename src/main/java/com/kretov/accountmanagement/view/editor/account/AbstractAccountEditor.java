package com.kretov.accountmanagement.view.editor.account;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.view.editor.AbstractEditor;
import com.vaadin.flow.component.textfield.TextField;

public abstract class AbstractAccountEditor extends AbstractEditor {
    protected TextField money = new TextField("Money");

    protected Account account;

    void processAccount(Account editableAccount, AccountService accountService) {
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
}
