package ru.vzotov.fx.dialog;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModalityHelper implements ListChangeListener<Node> {

    private static final Logger log = LoggerFactory.getLogger(ModalityHelper.class);

    private Node frontNode;

    @Override
    public void onChanged(Change<? extends Node> change) {
        while (change.next()) {
            // log.debug("permutated {}, added {}, replaced {}, removed {}, updated {}", change.wasPermutated(),
            // change.wasAdded(), change.wasReplaced(), change.wasRemoved(), change.wasUpdated());
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(node -> {
                    log.debug("Disable node {}", node);
                    node.setDisable(true);
                });

                updateFrontNode(change);
            } else if (change.wasRemoved()) {
                updateFrontNode(change);
            }
        }
    }

    private void updateFrontNode(Change<? extends Node> change) {
        final ObservableList<? extends Node> list = change.getList();
        if (!list.isEmpty()) {
            int size = list.size();
            final Node frontNode = list.get(size - 1);
            if (this.frontNode != null) {
                this.frontNode.setDisable(true);
                log.debug("Front node disabled: {}", this.frontNode);
            }
            this.frontNode = frontNode;
            frontNode.setDisable(false);
            log.debug("Front node enabled: {}", frontNode);
        }

    }
}
