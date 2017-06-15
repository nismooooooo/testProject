package com.main;

import com.classes.MyCommand;
import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.meta.application.OptionMetadata;

public class Test implements Module{

    static private OptionMetadata entity;
    static private OptionMetadata attribute;

    public static void main(String[] args) {

        entity = OptionMetadata
                .builder("entities", "Enter the entities count")
                .valueRequired("10").build();
        attribute = OptionMetadata
                .builder("attributes", "Enter the attributes count")
                .valueRequired("10").build();

        Bootique.app(args).module(Test.class).run();
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder).addCommand(MyCommand.class).addOption(entity).addOption(attribute);
    }
}
