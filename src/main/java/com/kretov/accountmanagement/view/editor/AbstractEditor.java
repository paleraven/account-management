package com.kretov.accountmanagement.view.editor;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractEditor extends VerticalLayout implements KeyNotifier {
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler handler) {
        changeHandler = handler;
    }

    protected ChangeHandler getChangeHandler() {
        return changeHandler;
    }
}
