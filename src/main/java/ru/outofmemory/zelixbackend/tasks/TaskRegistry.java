package ru.outofmemory.zelixbackend.tasks;

import org.springframework.stereotype.Component;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TaskRegistry {
    private final Map<MinerTask, ITaskHandler> handlers;

    public TaskRegistry(List<ITaskHandler> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(ITaskHandler::getType, h -> h));
    }

    public void handleTask(TasksWrapper tasksWrapper) {
        ITaskHandler handler = handlers.get(tasksWrapper.getTask());

        if (handler == null) {
            throw new IllegalArgumentException("No handler for task: " + tasksWrapper.getTask());
        }

        handler.handleTask(tasksWrapper);
    }
}