package business.generation;

import business.documents.Announcement;

public interface IGenerator {
    void generatePerson(String login);

    Announcement generateAnnouncement(String ownerId);
}