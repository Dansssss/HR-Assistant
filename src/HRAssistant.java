import java.io.*;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * COMP90041, Sem2, 2022: Final Project
 *
 * @author: Danlan Chen, danlan.chen@student.unimelb.edu.au, and 1288528.
 */
public class HRAssistant {

    public static void main(String[] args) {
        /*
        four variables to determine user input
         */
        boolean h = true;
        boolean a = false;
        boolean r = false;
        boolean j = false;

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // if input -h/--help, boolean h becomes true
        // System.out.println(args[0]);
        // System.out.println(args[1]);
        // System.out.println(args[2]);
        // System.out.println(args[3]);
        // System.out.println(args[4]);
        // System.out.println(args[5]);
        // System.out.println(args[6]);
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            h = true;
        } else {
            // a for loop to read input arguments
            for (int i = 0; i < args.length; i++) {

                // the last argument
                if (i == args.length - 1) {
                    // if no argument followed by -r, then no role is defined
                    if (args[i].equals("-r") || args[i].equals("--role")) {
                        System.out.println("ERROR: no role defined.");
                        System.exit(0);
                    }
                    // nothing is followed by -, then print help info
                    if (args[i].contains("-")) {
                        h = true;
                        break;
                    }
                }
                // if user input -r or --role
                if (args[i].equals("-r") || args[i].equals("--role")) {
                    if (!args[i + 1].contains("-")) {
                        stringStringHashMap.put("r", args[i + 1]);
                        r = true;
                        h = false;
                    } else {
                        System.out.println("ERROR: no role defined.");
                        System.exit(0);
                    }
                }
                // if user input -j or --jobs
                if (args[i].equals("-j") || args[i].equals("--jobs")) {
                    if (!args[i + 1].contains("-")) {
                        stringStringHashMap.put("j", args[i + 1]);
                        j = true;
                        h = false;
                    } else {
                        h = true;
                        break;
                    }
                }
                // if user input -a or --applications
                if (args[i].equals("-a") || args[i].equals("--applications")) {
                    if (!args[i + 1].contains("-")) {
                        stringStringHashMap.put("a", args[i + 1]);
                        a = true;
                        h = false;
                    } else {
                        h = true;
                        break;
                    }
                }
            }
        }
        // if h is true, print help information
        if (h) {
            displayHelp();
        }
        String filenameJ = "jobs.csv";
        String filenameA = "applications.csv";

        if (j) {
            filenameJ = stringStringHashMap.get("j");
            try {
                List<String> read = read(filenameJ);

                for (int i = 1; i <read.size() ; i++) {
                    String s = read.get(i);
                    s+=" ";
                    String[] split = s.split(",");
                    if (split.length!=6){
                        System.out.println("WARNING: invalid data format in jobs file in line "+(i+1));
                    }else {
                        if (split[3].equals("PHD")||split[3].equals("")||split[3].equals("Master")||split[3].equals("Bachelor")){

                        }else {
                            System.out.println("WARNING: invalid characteristic in jobs file in line "+(i+1));
                        }


                    }


                }
            } catch (IOException e) {
                System.out.println("file not exist");
            }
        }
        if (a) {
            filenameA = stringStringHashMap.get("a");
            try {
                List<String> read = read(filenameA);
                for (int i = 1; i < read.size(); i++) {
                    String s = read.get(i);
                    s+=" ";
                    String[] split = s.split(",");
                    if (split.length!=13){
                        System.out.println("WARNING: invalid data format in application file in line "+(i+1));
                    }else {
                        if (!isNumeric1(split[4])){
                            System.out.println("WARNING: invalid number format in applications file in line "+(i+1));
                        }
                        if (!(split[5].equals("")||split[5].equals("female")||split[5].equals("male"))){
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }
                        if (split[6].equals("PHD")||split[6].equals("")||split[6].equals("Master")||split[6].equals("Bachelor")){

                        }else {
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }
                        if (!(split[7].equals("")||isNumeric1(split[7])&&Integer.parseInt(split[7])>49&&Integer.parseInt(split[7])<=100)){
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }
                        if (!(split[8].equals("")||isNumeric1(split[8])&&Integer.parseInt(split[7])>49&&Integer.parseInt(split[7])<=100)){
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }
                        if (!(split[9].equals("")||isNumeric1(split[9])&&Integer.parseInt(split[7])>49&&Integer.parseInt(split[7])<=100)){
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }
                        if (!(split[10].equals("")||isNumeric1(split[10])&&Integer.parseInt(split[7])>49&&Integer.parseInt(split[7])<=100)){
                            System.out.println("WARNING: invalid characteristic in application file in line "+(i+1));
                        }

                    }

                }
            } catch (IOException e) {

            }

        }
        // if there is role defined
        if (r) {

            /*
            three roles go to three different interface
             */
            if (stringStringHashMap.get("r").equals("hr")) {
                // display welcome hr content
                displayWelcomeMessage("welcome_hr.ascii");
                // go to hr interface
                hrMenu(filenameJ, filenameA);

            } else if (stringStringHashMap.get("r").equals("applicant")) {
                // display welcome applicant content
                displayWelcomeMessage("welcome_applicant.ascii");

                // go to applicant interface
                applicantMenu(filenameJ, filenameA);


            } else if (stringStringHashMap.get("r").equals("audit")) {
                // go to audit interface
                auditMenu(filenameJ, filenameA);
            } else {
                System.out.println("ERROR: " + stringStringHashMap.get("r") + " is not a valid role.");
                displayHelp();
            }
        }


    }



    private static void auditMenu(String filenameJ, String filenameA){
        Scanner scanner = new Scanner(System.in);
        // read jobs list
        List<String> readJ = null;
        try {
            readJ = read("" + filenameJ);
        } catch (IOException e) {
            System.out.println("No jobs available for interrogation.");
            System.exit(0);
        }
        // read applicant list
        List<String> readA =null;
        try {
            readA = read("" + filenameA);
        } catch (IOException e) {
            System.out.println("No applicant available for interrogation.");
            System.exit(0);
        }
        // read aAndj.csv
        // aAndj.csv is to link applicant and job
        List<String> readAJ=null;
        try {
            readAJ = read("aAndj.csv");
        } catch (IOException e) {
            System.out.println("No aAndj available for interrogation.");
            System.exit(0);
        }
        HashMap<String, String> match = Matchmaker.match(readA, readJ, readAJ);

        String N = String.valueOf(readJ.size()-1);
        String M = String.valueOf(readAJ.size());

        System.out.println("======================================\n" +
                "# Matchmaking Audit\n" +
                "======================================");
        if (readJ.size()==0){
            System.out.println("No jobs available for interrogation.");

        }else {
            System.out.println("Available jobs: "+N);
            System.out.println("Total number of applicants: "+M);
            for (int i = 0; i <readJ.size() ; i++) {
                if (!readJ.get(i).contains("createdAt")){
                    String s = match.get(readJ.get(i)+" ");
                    if (s==null){
                        System.out.println("No applicants available for interrogation.");
                        System.exit(0);
                    }
                }
            }
            if (readJ.size()-1!=match.size()){
                System.out.println("No jobs available for interrogation.");
                System.exit(0);
            }

            // statistic
            System.out.println("Number of successful matches: "+match.size());
            int Q1=0;
            int Q2=0;
                /*
                calculate Q1
                 */
            for (String s : match.keySet()) {
                String s1 = match.get(s);
                String[] split = s1.split(",");
                if (isNumeric1(split[4])){
                    Q1+= Integer.parseInt(split[4]);
                }
                Q1 = Q1/match.size();
            }
                /*
                calculate Q2
                 */
            for (int i = 0; i < readA.size(); i++) {
                String s = readA.get(i);
                String[] split = s.split(",");
                if (isNumeric1(split[4])){
                    Q2+= Integer.parseInt(split[4]);
                }
                Q2 = Q2/readA.size();
            }

            String R1="n/a";
            String R2="n/a";
            double R1int=0;
            double R2int=0;
            double sum1=0;
            double sum2=0;
                /*
                    calculate R1
                 */
            for (String s : match.keySet()) {
                String s1 = match.get(s);
                String[] split = s1.split(",");
                if (isNumeric1(split[7])&&isNumeric1(split[8])&&isNumeric1(split[9])&&isNumeric1(split[10])){
                    double i = (Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + Integer.parseInt(split[10]))/4.0;
                    R1int+=i;
                    sum1++;
                }
            }
            if (R1int!=0){
                DecimalFormat df = new DecimalFormat("#.00");
                R1=  df.format(R1int/sum1);
            }

                /*
                calculate R2
                 */
            for (int i = 0; i < readA.size(); i++) {
                String s = readA.get(i);
                s+=" ";
                String[] split = s.split(",");
                if (isNumeric1(split[7])&&isNumeric1(split[8])&&isNumeric1(split[9])&&isNumeric1(split[10])){
                    double j = (Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + Integer.parseInt(split[10]))/4.0;
                    R2int+=j;
                    sum2++;
                }
            }
            if (R2int!=0){
                DecimalFormat df = new DecimalFormat("#.00");
                R2= df.format(R2int/sum2);
            }
            System.out.println("Average age: "+Q1+" (average age of all applicants: "+Q2+")");
            System.out.println("Average WAM: "+R1+"(average WAM of all applicants: "+R2+")");

                /*
                calculate gender, degree
                 */
            double male=0.00;
            double female=0.00;
            double sexmale=0;
            double sexfemale=0;
            double sexselectsum=readA.size();
            double PHD=0.00;
            double PHDSum=0.00;
            for (String s : match.keySet()) {
                String s1 = match.get(s);
                String[] split = s1.split(",");
                if (split[5].equals("male")){
                    sexmale++;
                }
                if (split[5].equals("female")){
                    sexfemale++;
                }
                if (split[6].equals("PHD")){
                    PHDSum++;
                }
            }
            male=sexmale/sexselectsum;
            female=sexfemale/sexselectsum;
            PHD=PHDSum/sexselectsum;

            if (female<=male&&PHD<=male){
                System.out.println("male:"+male);
                if (female<=PHD){
                    System.out.println("PHD:"+PHD);
                    System.out.println("female:"+female);
                }else {
                    System.out.println("female:"+female);
                    System.out.println("PHD:"+PHD);
                }


            } else if (male<=female&&PHD<=female) {
                System.out.println("female:"+female);
                if (male<=PHD){
                    System.out.println("PHD:"+PHD);
                    System.out.println("male:"+male);
                }else {
                    System.out.println("male:"+male);
                    System.out.println("PHD:"+PHD);
                }


            }else if (male<=PHD&&female<=PHD){
                System.out.println("PHD:"+PHD);
                if (male<=female){
                    System.out.println("female:"+female);
                    System.out.println("male:"+male);
                }else {
                    System.out.println("male:"+male);
                    System.out.println("female:"+female);
                }

            }


        }



    }


    /**
     * hr interface
     *
     * @param filenameJ JOBS argument
     * @param filenameA applicant argument
     */
    private static void hrMenu(String filenameJ, String filenameA) {
        Scanner scanner = new Scanner(System.in);

        // read jobs list
        List<String> readJ = null;
        try {
            readJ = read("" + filenameJ);
        } catch (IOException e) {
            System.out.println("No jobs available for interrogation.");
        }
        // read applicants list
        List<String> readA = null;
        try {
            readA = read("" + filenameA);
        } catch (IOException e) {
            System.out.println("No applicants available for interrogation.");
        }
        // read aAndj list
        List<String> readAJ = null;
        try {
            readAJ = read("aAndj.csv");
        } catch (IOException e) {
            System.out.println("No aAndj available for interrogation.");
        }
        int N=readAJ.size()-1;
        boolean flag=true;
        String hr="";
        while (true) {


            if (flag){
                System.out.println(N+" applications received.");
                System.out.println("Please enter one of the following commands to continue:");
                System.out.println("- create new job: [create] or [c]");
                System.out.println("- list available jobs: [jobs] or [j]");
                System.out.println("- list applicants: [applicants] or [a]");
                System.out.println("- filter applications: [filter] or [f]");
                System.out.println("- matchmaking: [match] or [m]");
                System.out.println("- quit the program: [quit] or [q]");
                System.out.print("> ");
                hr = scanner.nextLine();
            }

            if (hr.equals("c") || hr.equals("create")) {
                flag=true;
                // create a new job
                System.out.println("# Create new Job");
                System.out.print("Position Title: ");
                String positionTitle = scanner.nextLine();
                positionTitle= positionTitle.replaceAll(",",".");
                while (positionTitle.equals("")) {
                    System.out.println("Ooops! Position Title must be provided: ");
                    positionTitle = scanner.nextLine();
                }
                System.out.print("Position Description: ");
                String positionDescription = scanner.nextLine();
                positionDescription=positionDescription.replaceAll(",",".");
                System.out.print("Minimum Degree Requirement: ");
                String minimumDegreeRequirement = scanner.nextLine();

                while (!(minimumDegreeRequirement.equals("Bachelor") || minimumDegreeRequirement.equals("Master") || minimumDegreeRequirement.equals("") || minimumDegreeRequirement.equals("PHD"))) {
                    System.out.println("Invalid input! Please specify Minimum Degree Requirement: ");
                    minimumDegreeRequirement = scanner.nextLine();
                }
                System.out.print("Salary ($ per annum): ");
                String salary = scanner.nextLine();
                while (!(salary.equals("") || isNumeric1(salary) && Integer.parseInt(salary) > 0)) {
                    System.out.println("Invalid input! Please specify Salary Expectations: ");
                    //System.out.print("Salary Expectations ($ per annum): ");
                    salary = scanner.nextLine();
                }
                System.out.print("Start Date: ");
                String startDate = scanner.nextLine();
                while (startDate.equals("")) {
                    System.out.println("Ooops! Start Date must be provided: ");
                    startDate = scanner.nextLine();
                }
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                String[] split = startDate.split("/");


                while (!((isNumeric1(split[0])&&isNumeric1(split[1])&&isNumeric1(split[2]))&&((Integer.parseInt(split[0])<=31)&&(Integer.parseInt(split[1])<=12)&&(Integer.parseInt(split[1])>=1)&&(Integer.parseInt(split[0])>=1)))){
                    System.out.print("Invalid input! Please specify Start Date: ");
                    startDate = scanner.nextLine();
                    if (startDate.equals("")){
                        break;
                    }
                    split = startDate.split("/");
                }
                try {
                    long time = new Date().getTime();
                    while (!(dateStrIsValid(startDate) && format.parse(startDate).getTime() > time)) {
                        System.out.print("Invalid input! Please specify Start Date: ");
                        startDate = scanner.nextLine();
                        if (startDate.equals("")){
                            break;
                        }
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                // create a random number as the createdAt value
                long createdAt = System.currentTimeMillis();

                String res = new String(createdAt + "," + positionTitle + "," + positionDescription + "," + minimumDegreeRequirement + "," + salary + "," + (startDate));
                ArrayList<String> strings = new ArrayList<>();
                strings.add(res);
                // write in all information just created
                boolean write = write(strings, filenameJ);
            } else if (hr.equals("j") || hr.equals("jobs")) {
                flag=true;
                // display jobs
                if (readJ.size()-1 == 0) {
                    System.out.println("No jobs available.");
                } else {
                    // for loop to print jobs
                    try {
                        readJ=read(filenameJ);
                    } catch (IOException e) {
                        System.out.println("No jobs available.");
                    }

                    for (int i = 1; i < readJ.size(); i++) {
                        String s = readJ.get(i);
                        s=s+"\r";
                        String[] split = s.split(",");
                        // print jobs list
                        String s1 = new String("[" + i + "] " + (split[1].equals("")?"n/a":split[1]) + "(" + (split[2].equals("")?"n/a":split[2]) + "). " + (split[3].equals("")?"n/a":split[3])  + ". Salary:" +  (split[4].equals("")?"n/a":split[4]) + ". Start Date:" + (split[5].equals("\r")?"n/a":split[5]));
                        System.out.println(s1);
                        //
                        int wordInt = 0;
                        // print jobs
                        for (int j = 0; j < readAJ.size(); j++) {
                            String s2 = readAJ.get(j);
                            String[] split1 = s2.split(",");
                            // determine if there is applicant under this job
                            if (split1[0].equals(split[0])) {
                                for (int k = 0; k < readA.size(); k++) {
                                    String s3 = readA.get(k);
                                    s3=s3+"\r";
                                    String[] split2 = s3.split(",");
                                    // determine who the applicant is
                                    if (split1[1].equals(split2[0])) {
                                        char word = (char) (wordInt + 97);
                                        wordInt++;
                                        String s4 = new String("    [" + word + "]" + (split2[1].equals("")?"n/a":split2[1]) + "," + (split2[2].equals("")?"n/a":split2[2])+ "(" + (split2[6].equals("") ? "n/a" : split2[6]) + "):" + (split2[3].equals("")?"n/a":split2[3]) + ".Salary Expectations:" + (split2[11].equals("")?"n/a":split2[11]) + ". Available:" + (split2[12].equals("\r")?"n/a":split2[12]));
                                        System.out.println(s4);
                                    }
                                }
                            }
                        }


                    }
                }
            } else if (hr.equals("a") || hr.equals("applicants")) {
                flag=true;
                // for loop to print applicants
                if (readA.size()-1==0){
                    System.out.println("No applicants available.");
                }else {
                    for (int i = 1; i < readA.size(); i++) {
                        String s = readA.get(i);
                        s=s+"\r";
                        String[] split2= s.split(",");
                        String s1 = new String("[" + i + "]" + (split2[1].equals("") ? "n/a" : split2[1]) + "," + (split2[2].equals("") ? "n/a" : split2[2]) + "(" + (split2[6].equals("") ? "n/a" : split2[6]) + "):" + (split2[3].equals("") ? "n/a" : split2[3]) + ".Salary Expectations:" + (split2[11].equals("") ? "n/a" : split2[11]) + ". Available:" + (split2[12].equals("\r") ? "n/a" : split2[12]));
                        System.out.println(s1);
                    }
                }

            }else if (hr.equals("f") || hr.equals("filter")){
                flag=true;
                if (readA.size()-1==0){
                    System.out.println("No applicants available.");
                }else {
                    System.out.println("Filter by: [lastname], [degree] or [wam]: ");
                    String filter = scanner.nextLine();
                    if (filter.equals("lastname")){

                        //alphabetical order according to last name
                        Collections.sort(readA, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                o1+="\r";
                                o2+="\r";
                                String[] split = o1.split(",");
                                String[] split1 = o2.split(",");
                                if (split1[1].charAt(0)>split[1].charAt(0)){
                                    return -1;
                                }else if (split1[1].charAt(0)<split[1].charAt(0)){
                                    return 1;
                                }else {
                                    return 0;
                                }

                            }
                        });
                    } else if (filter.equals("degree")) {
                        // descending order according degree
                        Collections.sort(readA, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                o1+="\r";
                                o2+="\r";
                                String[] split = o1.split(",");
                                String[] split1 = o2.split(",");
                                if (split[6].equals("")){
                                    return 1;
                                } else if (split1[6].equals("")) {
                                    return -1;
                                }

                                if (split1[6].charAt(0)>split[6].charAt(0)){
                                    return 1;
                                }else if (split1[6].charAt(0)<split[6].charAt(0)){
                                    return -1;
                                }else {
                                    return 0;
                                }


                            }
                        });
                    } else if (filter.equals("wam")) {

                        // descending order according wam
                        Collections.sort(readA, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                o1+="\r";
                                o2+="\r";
                                String[] split = o1.split(",");
                                String[] split1 = o2.split(",");
                                if (split1[7].equals("")||split1[8].equals("")||split1[9].equals("")||split1[10].equals("")||!isNumeric1(split1[7])){
                                    if (split1[1].charAt(0)>split[1].charAt(0)){
                                        return -1;
                                    }else if (split1[1].charAt(0)<split[1].charAt(0)){
                                        return 1;
                                    }else {
                                        return 0;
                                    }
                                } else if (split[7].equals("")||split[8].equals("")||split[9].equals("")||split[10].equals("")||!isNumeric1(split1[7])) {
                                    if (split1[1].charAt(0)>split[1].charAt(0)){
                                        return -1;
                                    }else if (split1[1].charAt(0)<split[1].charAt(0)){
                                        return 1;
                                    }else {
                                        return 0;
                                    }
                                }else {
                                    if ((Integer.parseInt(split1[7])+Integer.parseInt(split1[8])+Integer.parseInt(split1[9])+Integer.parseInt(split1[10]))/4>(Integer.parseInt(split[7])+Integer.parseInt(split[8])+Integer.parseInt(split[9])+Integer.parseInt(split[10]))/4){
                                        return 1;
                                    } else if ((Integer.parseInt(split1[7])+Integer.parseInt(split1[8])+Integer.parseInt(split1[9])+Integer.parseInt(split1[10]))/4<(Integer.parseInt(split[7])+Integer.parseInt(split[8])+Integer.parseInt(split[9])+Integer.parseInt(split[10]))/4) {
                                        return -1;
                                    }else {
                                        return 0;
                                    }
                                }

                            }
                        });
                    }

                    for (int i = 0; i < readA.size(); i++) {
                        if (!readA.get(i).contains("createdAt")){
                            System.out.println(readA.get(i));
                        }
                    }
                }

            }else if (hr.equals("m") || hr.equals("match")){
                flag=true;
                HashMap<String, String> match = Matchmaker.match(readA, readJ, readAJ);
                int i=0;
                for (String s : match.keySet()) {
                    i++;
                    String s1 = match.get(s);
                    s1+="\r";
                    s+="\r";
                    String[] split = s.split(",");
                    String s11 = new String("[" + i + "] " + split[1] + "(" + split[2] + "). " + split[3] + ". Salary:" + split[4] + ". Start Date:" + split[5]);
                    System.out.println(s11);
                    String[] split2 = s1.split(",");
                    String s4 = new String("    [a]" + split2[1] + "," + split2[2] + "(" + (split2[6].equals("") ? "n/a" : split2[6]) + "):" + split2[4] + ".Salary Expectations:" + split2[11] + ". Available:" + split2[12]);
                    System.out.println(s4);
                }
            } else if (hr.equals("q") || hr.equals("quit")) {
                System.out.println();
                System.exit(0);
            } else {
                System.out.print("Invalid input! Please enter a valid command to continue: \n> ");
                hr=scanner.nextLine();
                flag=false;
            }
        }
    }

    /**
     * applicant interface
     *
     * @param filenameJ JOBS argument
     * @param filenameA applicant argument
     */
    private static void applicantMenu(String filenameJ, String filenameA) {
        Scanner scanner = new Scanner(System.in);
        // determine if c is display
        boolean flagCreate = true;
        boolean flag=true;
        String application="";
        // store the created applicant id
        String aid = "";

        // read jobs list
        List<String> read = null;
        try {
            read = read("" + filenameJ);
        } catch (IOException e) {
            System.out.println("No jobs available for interrogation.");
        }
        int N = read.size()-1;
        int M =0;
        // submitted application
        ArrayList<Integer> disitme = new ArrayList<>();
        // store applied jobs id and applicant
        ArrayList<String> saveList = new ArrayList<>();
        while (true) {
            if(flag) {
                System.out.println(N + " jobs available. " + M + " applications submitted.");
                System.out.println("Please enter one of the following commands to continue:");
                if (flagCreate) {
                    System.out.println("- create new application: [create] or [c]");
                }
                System.out.println("- list available jobs: [jobs] or [j]");
                System.out.println("- quit the program: [quit] or [q]");
                System.out.print("> ");
                application = scanner.nextLine();}
            if ((application.equals("c") || application.equals("create"))&&flagCreate) {
                // create application
                flag=true;
                System.out.println("# Create new Application");
                System.out.print("Lastname: ");
                String lastName = scanner.nextLine();
                while (lastName.equals("")) {
                    System.out.println("Ooops! Lastname must be provided: ");
                    lastName = scanner.nextLine();
                }
                System.out.print("Firstname: ");
                String firstName = scanner.nextLine();
                while (firstName.equals("")) {
                    System.out.println("Ooops! Firstname must be provided: ");
                    firstName = scanner.nextLine();
                }
                System.out.print("Career Summary: ");
                String careerSummary = scanner.nextLine();
                careerSummary=careerSummary.replaceAll(",",".");
                System.out.print("Age: ");
                String age = scanner.nextLine();
                while (age.equals("")) {
                    System.out.println("Ooops! Age must be provided: ");
                    age = scanner.nextLine();
                }

                while (!(isNumeric1(age) && Integer.parseInt(age) > 18 && Integer.parseInt(age) < 100)) {
                    System.out.println("0oops! A valid age between 18 and 100 must be provided: ");
                    age = scanner.nextLine();
                }
                System.out.print("Gender: ");
                String gender = scanner.nextLine();
                while (!(gender.equals("female") || gender.equals("male") || gender.equals("") || gender.equals("other"))) {
                    System.out.println("Invalid input! Please specify Gender: ");
                    gender = scanner.nextLine();
                }
                System.out.print("Highest Degree: ");
                String highestDegree = scanner.nextLine();
                while (!(highestDegree.equals("Bachelor") || highestDegree.equals("Master") || highestDegree.equals("") || highestDegree.equals("PHD"))) {
                    System.out.print("Invalid input! Please specify Highest Degree: ");
                    highestDegree = scanner.nextLine();
                }
                System.out.println("Coursework: ");
                System.out.print("- COMP90041: ");
                String COMP90041 = scanner.nextLine();
                while (!(COMP90041.equals("") || (isNumeric1(COMP90041) && Integer.parseInt(COMP90041) >= 49 && Integer.parseInt(COMP90041) <= 100))) {
                    System.out.println("Invalid input! Please specify COMP90041: ");
                    COMP90041 = scanner.nextLine();
                }
                System.out.print("- COMP90038: ");
                String COMP90038 = scanner.nextLine();
                while (!(COMP90038.equals("") || (isNumeric1(COMP90038) && Integer.parseInt(COMP90038) >= 49 && Integer.parseInt(COMP90038) <= 100))) {
                    System.out.println("Invalid input! Please specify COMP90038: ");
                    COMP90038 = scanner.nextLine();
                }
                System.out.print("- COMP90007: ");
                String COMP90007 = scanner.nextLine();
                while (!(COMP90007.equals("") || (isNumeric1(COMP90007) && Integer.parseInt(COMP90007) >= 49 && Integer.parseInt(COMP90007) <= 100))) {
                    System.out.println("Invalid input! Please specify COMP90007: ");
                    COMP90007 = scanner.nextLine();
                }
                System.out.print("- INFO90002: ");
                String INFO90002 = scanner.nextLine();
                while (!(INFO90002.equals("") || (isNumeric1(INFO90002) && Integer.parseInt(INFO90002) >= 49 && Integer.parseInt(INFO90002) <= 100))) {
                    System.out.println("Invalid input! Please specify INFO90002: ");
                    INFO90002 = scanner.nextLine();
                }
                System.out.print("Salary Expectations ($ per annum): ");
                String salary = scanner.nextLine();
                while (!(salary.equals("") || isNumeric1(salary) && Integer.parseInt(salary) > 0)) {
                    System.out.println("Invalid input! Please specify Salary Expectations: ");
                    salary = scanner.nextLine();
                }
                System.out.print("Availability: ");
                String availability = scanner.nextLine();
                if (!availability.equals("")) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                    String[] split = availability.split("/");

                    while (!((isNumeric1(split[0]) && isNumeric1(split[1]) && isNumeric1(split[2])) && ((Integer.parseInt(split[0]) <= 31) && (Integer.parseInt(split[1]) <= 12) && (Integer.parseInt(split[1]) >= 1) && (Integer.parseInt(split[0]) >= 1)))) {
                        System.out.println("Invalid input! Please specify Availability: ");
                        availability = scanner.nextLine();
                        if (availability.equals("")){
                            break;
                        }
                        split = availability.split("/");
                    }
                    try {
                        long time = new Date().getTime();
                        while (!(dateStrIsValid(availability) && format.parse(availability).getTime() > time)) {
                            System.out.println("Invalid input! Please specify Availability: ");
                            availability = scanner.nextLine();
                            if (availability.equals("")){
                                break;
                            }
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                // create a random number as the createdAt value
                long createdAt = System.currentTimeMillis();
                // put together input
                String res = new String(createdAt + "," + lastName + "," + firstName + "," + careerSummary + "," + age + "," + gender + "," + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007 + "," + INFO90002 + "," + salary + "," + (availability.equals(" ")?" ":availability));
                ArrayList<String> strings = new ArrayList<>();
                strings.add(res);
                // write into file
                boolean write = write(strings, filenameA);
                // hide c
                if (write) {
                    flagCreate = false;
                    aid = String.valueOf(createdAt);
                }
                // end of create
            } else if (application.equals("j") || application.equals("jobs")) {
                flag=true;

                // if there is no job
                if (read.size() -1== 0) {
                    System.out.println("No jobs available.");
                } else {
                    // for loop to print jobs
                    for (int i = 1; i < read.size(); i++) {
                        String s = read.get(i);
                        s+="\r";
                        String[] split = s.split(",");
                        // put together output
                        String s1 = new String("[" + i + "] " + split[1] + "(" + split[2] + "). " + split[3] + ". Salary:" + split[4] + ". Start Date:" + split[5]);
                        System.out.println(s1);
                    }
                    if (!(read.size()==1)&&!aid.equals("")) {
                        System.out.print("Please enter the jobs you would like to apply for (multiple options are possible): ");
                        String s = scanner.nextLine();
                        if (!s.equals("")) {
                            if (s.contains(",")) {
                                String[] disres = s.split(",");
                                for (int i = 0; i < disres.length; i++) {
                                    boolean numeric1 = isNumeric1(disres[i]);
                                    if (numeric1) {
                                        if (Integer.parseInt(disres[i])<read.size()&&Integer.parseInt(disres[i])>0){
                                            disitme.add(Integer.parseInt(disres[i]));
                                        }else {
                                            System.out.println("Invalid input! Please enter a valid number to continue: ");
                                        }
                                    }
                                }
                                // set M N
                                // delete applied job next time input j
                                M=disitme.size()+M;
                                N=(read.size()-1-disitme.size());
//                                flagJobs = true;
                                // sort the list
                                Collections.sort(disitme);
                                // reverse the order from more to less
                                Collections.reverse(disitme);
                            } else {
                                boolean numeric1 = isNumeric1(s);
                                if (numeric1) {
                                    if (Integer.parseInt(s)<read.size()&&Integer.parseInt(s)>0){
//                                        flagJobs = true;
                                        disitme.add(Integer.parseInt(s));
                                        M=disitme.size()+M;
                                        N=(read.size()-1-disitme.size());
                                    }else {
                                        System.out.println("Invalid input! Please enter a valid number to continue: ");
                                    }

                                }
                            }
                        } else {

                        }
                    }else {
                        if (!aid.equals("")){
                            System.out.println("No jobs available.");
                        }
                    }
                }
                // for loop to remove applied job
                for (int j = 0; j < disitme.size(); j++) {
                    String s = read.get(disitme.get(j).intValue());
                    String[] split = s.split(",");
                    String s1 = new String(split[0] + "," + aid);
                    saveList.add(s1);
                    boolean write = write(saveList, "aAndj.csv");
                    if (write) {
                        read.remove(disitme.get(j).intValue());
                        saveList.clear();
                    }
                }

                disitme.clear();
            } else if (application.equals("quit") || application.equals("q")) {
                System.out.println();
                System.exit(0);
            } else {
                System.out.print("Invalid input! Please enter a valid command to continue:\n> ");
                application = scanner.nextLine();
                flag=false;
            }
        }
    }

    /**
     * check if input date is valid
     *
     * @param rawDateStr string waiting to be checked
     * @return return true if it is, otherwise return false
     */
    public static boolean dateStrIsValid(String rawDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = null;
        try {
            // change the format to Date
            date = dateFormat.parse(rawDateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }

    }


    /**
     * check if it is a number
     *
     * @param str the string to be checked
     * @return return true if it is, otherwise throw Exception
     */
    public static boolean isNumeric1(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * read file line by line
     *
     * @param name input file
     * @return file content
     */
    private static List<String> read(String name) throws IOException {
        FileInputStream fileInputStream = null;
        ArrayList<String> strings = new ArrayList<>();

        fileInputStream = new FileInputStream(name);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {

            strings.add(s);
        }
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();


        return strings;
    }

    /**
     * write into file line by line
     *
     * @param list content to write in
     * @param filename output file
     * @return
     */
    public static boolean write(List<String> list, String filename) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("" + filename), true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < list.size(); i++) {

                bufferedWriter.write(list.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    /**
     * print help information
     */
    private static void displayHelp() {
        System.out.println("HRAssistant - COMP90041 - Final Project\n\n" +
                "Usage: java HRAssistant [arguments]\n\n" +
                "Arguments:\n" +
                "    -r or --role            Mandatory: determines the user's role\n" +
                "    -a or --applications    Optional: path to applications file\n" +
                "    -j or --jobs            Optional: path to jobs file\n" +
                "    -h or --help            Optional: print Help (this message) and exit");
    }

    // feel free to modify this
    private static void displayWelcomeMessage(String filename) {

        Scanner inputStream = null;

        try {
            inputStream = new Scanner(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Welcome File not found.");
        }

        while (inputStream.hasNextLine()) {
            System.out.println(inputStream.nextLine());
        }
    }
}
