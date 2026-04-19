package ru.outofmemory.zelixbackend.tasks;

import ru.outofmemory.zelixbackend.utilities.MinerTask;

public interface ITaskHandler {
    MinerTask getType();
    void handleTask(TasksWrapper tasksWrapper);
}
