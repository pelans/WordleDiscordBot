package org.pelans.wordle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.pelans.wordle.Database.Services.ServerWordService;
import org.pelans.wordle.Discord.Discord;
import org.pelans.wordle.Database.Entities.ServerWord;
import org.pelans.wordle.util.HibernateUtil;

import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            //Load config.properties Must be configured before executing this program.
            Properties prop =new Properties();
            InputStream ip = Main.class.getResourceAsStream("/config.properties");
            prop.load(ip);

            //Load wordle words
            Wordle.init();

            //Load discord bot
            Discord.configureDiscordBot(prop.getProperty("DiscordBotToken"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    private static void loadDatabase() {
        /*final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        ServerWord serverWord = new ServerWord("serverTest",Wordle.getWord());

        session.save(serverWord);


        transaction.commit();

        session.close();




        factory.close(); */
        /*
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        ServerWord serverWord = new ServerWord("serverTest",Wordle.getWord());
        session.persist(serverWord);
        transaction.commit();
        session.close();

        session = HibernateUtil.openSession();
        transaction = session.beginTransaction();
        serverWord = new ServerWord("serverTest",Wordle.getWord());
        session.persist(serverWord);
        transaction.commit();
        session.close();

        ServerWord serverWord2 = ServerWordService.getServerWord("serverTest");
        int a; */





    }


}