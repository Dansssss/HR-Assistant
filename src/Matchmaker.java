import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contains the logic for your matchmaking algorithm
 *
 * @author: Danlan Chen, danlan.chen@student.unimelb.edu.au, and 1288528.
 */

public class Matchmaker {

    public Matchmaker() {

    }

    /**
     * matchmaker
     *
     * @return selected applicant
     */
    public static HashMap<String, String> match(List<String> readA, List<String> readJ, List<String> readAJ) {
        // display jobs
        if (readJ.size() == 0) {
            System.out.println("No jobs available.");
        } else {
            if (readA.size() == 0) {
                System.out.println("No applicants available.");
            } else {
                List<String> readACopy = new ArrayList<>();

                Collections.addAll(readACopy,  new  String[readA.size()]);
                Collections.copy(readACopy,readA );
                // store
                HashMap<String, String> objectObjectHashMap = new HashMap<>();
                // store characteristics
                HashMap<String, ArrayList<String>> stringArrayListHashMap = new HashMap<>();
                // for loop to print jobs
                for (int i = 1; i < readJ.size(); i++) {
                    String s = readJ.get(i);
                    s+=" ";
                    String[] split = s.split(",");
                    // put together
                    String s1 = new String("[" + i + "] " + split[1] + "(" + split[2] + "). " + split[3] + ". Salary£º" + split[4] + ". Start Date:" + split[5]);
                    stringArrayListHashMap.put(s1, new ArrayList<String>());
                    // for loops to print
                    for (int j = 0; j < readAJ.size(); j++) {
                        String s2 = readAJ.get(j);
                        String[] split1 = s2.split(",");
                        // check if there is applicant under this job
                        if (split1[0].equals(split[0])) {
                            for (int k = 0; k < readA.size(); k++) {
                                String s3 = readA.get(k);
                                s3+=" ";

                                String[] split2 = s3.split(",");
                                // print the applicant
                                if (split1[1].equals(split2[0])) {
                                    // check if it fit the minimal degree

                                    if ((!split[3].equals("")&&!split2[6].equals(""))&&(split[3].charAt(0) <= split2[6].charAt(0))) {
                                        // put applicants who fit the minimal degree into map then continue
                                        ArrayList<String> strings = stringArrayListHashMap.get(s1);
                                        strings.add(s3);
                                    }
                                }
                            }
                        }
                    }


                }

                int flag = 0;
                for (int i = 0; i < readJ.size(); i++) {
                    String s = readJ.get(i);
                    s+=" ";
                    String[] split = s.split(",");
                    // put together
                    String s1 = new String("[" + i + "] " + split[1] + "(" + split[2] + "). " + split[3] + ". Salary£º" + split[4] + ". Start Date:" + split[5]);

                    ArrayList<String> strings = stringArrayListHashMap.get(s1);

                    if (strings == null || strings.size() == 0) {

                    } else {
                        flag++;
                        // select the youngest
                        Collections.sort(strings, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                String[] split = o1.split(",");
                                String[] split1 = o2.split(",");
                                if ((HRAssistant.isNumeric1(split[4]) && HRAssistant.isNumeric1(split1[4])) && (Integer.parseInt(split[4]) > Integer.parseInt(split1[4]))) {
                                    return 1;
                                } else if ((HRAssistant.isNumeric1(split[4]) && HRAssistant.isNumeric1(split1[4])) && (Integer.parseInt(split[4]) < Integer.parseInt(split1[4]))) {
                                    return -1;
                                } else {
                                    if (split1[12].equals(" ")||split1[12].equals("")){
                                        return 0;
                                    }else {
                                        // if the same age, select the one who is the most recent available
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                                        try {
                                            if (simpleDateFormat.parse(split[12]).getTime() > simpleDateFormat.parse(split1[12]).getTime()) {
                                                return 1;
                                            } else if (simpleDateFormat.parse(split[12]).getTime() > simpleDateFormat.parse(split1[12]).getTime()) {
                                                return -1;
                                            } else {
                                                return 0;
                                            }
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
//

                                }

                            }
                        });




                        String s2 = "";
                        for (int j = 0; j <stringArrayListHashMap.get(s1).size() ; j++) {

                            s2 = stringArrayListHashMap.get(s1).get(j);
                            s2=s2.trim();
                            if (readACopy.contains(s2)){
                                readACopy.remove(s2);
                                break;
                            }else {
                                s2="";
                            }
                        }
                        if (!s2.equals("")){

                            objectObjectHashMap.put(s,s2);
                        }



                    }
                }
                if (flag == 0) {
                    System.out.println("No applicants available.");
                }
                return objectObjectHashMap;
            }


        }

        return null;
    }


}