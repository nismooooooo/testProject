package com.classes;

import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;

public class MyCommand extends CommandWithMetadata {

    private static CommandMetadata createMetadata() {
        return CommandMetadata.builder(MyCommand.class)
                .description("Invoke the run method")
                .build();
    }

    public MyCommand() {
        super(createMetadata());
    }

    @Override
    public CommandOutcome run(Cli cli) {

        GenerateObjects generateObjects = new GenerateObjects();
        generateObjects.createTestProject(cli.optionString("entities"), cli.optionString("attributes"));

        return CommandOutcome.succeeded();
    }
}