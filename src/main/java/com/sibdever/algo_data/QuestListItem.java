package com.sibdever.algo_data;

import com.sibdever.algo_data.quest.Quest;

// Todo are you really need it?
public class QuestListItem {

    private Quest quest;
    private QuestStatus status;

    public QuestListItem(Quest quest, QuestStatus status) {
        this.quest = quest;
        this.status = status;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }
}
