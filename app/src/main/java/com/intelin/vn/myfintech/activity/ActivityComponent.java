package com.intelin.vn.myfintech.activity;

import dagger.Component;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 01/04/2019
 * Time: 1:54 PM
 */
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    ActivitySaved getFunction();
}

