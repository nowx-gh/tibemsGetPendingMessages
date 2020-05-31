import javax.jms.*;
import java.util.Enumeration;
import java.util.Calendar;
import java.util.Date;

public class tibjmsGetPendingMessages
{
    String      serverUrl       = null;
    String      userName        = null;
    String      password        = null;
    String      queueName       = null;
    String      selector        = null;
    int         beforeMinutes   = 0;

    public tibjmsGetPendingMessages(String[] args)
    {
        parseArgs(args);

        try
        {
            tibjmsUtilities.initSSLParams(serverUrl,args);
        }
        catch (JMSSecurityException e)
        {
            System.err.println("JMSSecurityException: "+e.getMessage()+", provider="+e.getErrorCode());
            e.printStackTrace();
            System.exit(0);
        }
        /* 
        System.err.println("Using server:   "+serverUrl);
        System.err.println("Browsing queue: "+queueName);
        */ 
        try
        {
            // convert to unix timestamp (milliseconds)
            ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);
            Connection connection = factory.createConnection(userName,password);
            Session session = connection.createSession();
            javax.jms.Queue queue = session.createQueue(queueName);
            javax.jms.Message message = null;
            connection.start();

             Calendar calendar = Calendar.getInstance();
             if (beforeMinutes != 0)
             {
                calendar.add(Calendar.MINUTE, -beforeMinutes);
                Date date = calendar.getTime();
                //System.err.println(date);
                Long unixTimestamp = date.getTime();
                selector = "JMSTimestamp < " + unixTimestamp;
                //System.err.println(unixTimestamp);
             }

            /*
             * create browser and browse what is there in the queue
             */

            javax.jms.QueueBrowser browser = session.createBrowser(queue, selector);
            Enumeration msgs = browser.getEnumeration();
            int browseCount=0;

            while (msgs.hasMoreElements())
            {
                message = (javax.jms.Message)msgs.nextElement();
                /* System.err.println("Browsed message: number="+message.getIntProperty("msg_num")); */
                browseCount++;
            }

            System.err.println("Found "+ browseCount + " messages in " + queueName);

            /*
             * close all and quit
             */
            browser.close();
            connection.close();
        }
        catch (JMSException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String args[])
    {
        tibjmsGetPendingMessages t = new tibjmsGetPendingMessages(args);
    }

    void usage()
    {
        System.err.println("\nUsage: java tibjmsGetPendingMessages [options]");
        System.err.println("");
        System.err.println("  where options are:");
        System.err.println("");
        System.err.println("  -server   <server-url>    - EMS server URL, default is local server");
        System.err.println("  -user     <user-name>     - user name, default is null");
        System.err.println("  -password <password>      - password, default is null");
        System.err.println("  -queue    <queue-name>    - queue name, default is null");
        System.err.println("  -selector <selector-name> - selector string, default is empty aka w/o selector. Can't be used with other filters");
        System.err.println("  -before-hours <hours>     - only count messages before ? hours. Can't be used with other filters");
        System.err.println("  -before-minutes <minutes> - only count messages before ? minutes. Can't be used with other filters");
        System.exit(0);
    }

    void parseArgs(String[] args)
    {
        int i=0;
        while (i < args.length)
        {
            if (args[i].compareTo("-server")==0)
            {
                if ((i+1) >= args.length) usage();
                serverUrl = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-user")==0)
            {
                if ((i+1) >= args.length) usage();
                userName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-password")==0)
            {
                if ((i+1) >= args.length) usage();
                password = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-queue")==0)
            {
                if ((i+1) >= args.length) usage();
                queueName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-selector")==0)
            {
                if ((i+1) >= args.length) usage();
                if (beforeMinutes != 0) usage();
                selector = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-before-hours")==0)
            {
                if ((i+1) >= args.length) usage();
                if (selector != null) usage();
                if (beforeMinutes != 0) usage();
                beforeMinutes = Integer.parseInt(args[i+1]) * 60;
                i += 2;
            }
            else
            if (args[i].compareTo("-before-minutes")==0)
            {
                if ((i+1) >= args.length) usage();
                if (selector != null) usage();
                beforeMinutes = Integer.parseInt(args[i+1]);
                i += 2;
            }
            else
            if (args[i].compareTo("-help")==0)
            {
                usage();
            }
            else
            {
                System.err.println("Unrecognized parameter: "+args[i]);
                usage();
            }
        }
    }

}


