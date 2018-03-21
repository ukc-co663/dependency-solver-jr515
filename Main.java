// @author JR515

import java.util.*;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    private static JSONParser parser = new JSONParser();
    private static JSONArray con; //array of Objects and end goal
    private static JSONArray ini; //array of String and starting state
    private static List<Pack> packRep = new ArrayList();
    private static JSONArray cList = ini;
    
    public static void main(String[] args) {
        try {
            JSONArray rep = (JSONArray) parser.parse(new FileReader(args[0]));
            con = (JSONArray) parser.parse(new FileReader(args[2]));
            ini = (JSONArray) parser.parse(new FileReader(args[1]));
            
            for (Object o : rep) {
                JSONObject jo = (JSONObject) o;
                Pack p = new Pack();
                p.setName((String) jo.get("name"));
                p.setVersion((String) (jo.get("version")));
                p.setSize((Long) jo.get("size"));
                JSONArray dList = (JSONArray) jo.get("depends");
                JSONArray cList = (JSONArray) jo.get("conflicts");
                p.setDepends(dList);
                p.setConflicts(cList);
                packRep.add(p);
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pack> nList = sortPList(getPList("A", packRep));
        List<Pack> fList = search(nList);
        if (false) for(Pack p : fList) {
          System.out.println(chkDep(p, ini));
          System.out.println("Name:" +p.getName() + " Ver:" + p.getVersion());  
        }
        System.out.println("" + fList);
        
        //getAll(packRep);
    }
    
    public static void run() {
        
        for(Object o : con) {
            String s = o.toString();
            s = s.substring(1);
            List<Pack> nList = sortPList(getPList(s, packRep));
            List<Pack> fList = search(nList);        
            
        }
    }
    
    public static List search(List<Pack> pList) {
        for(Pack p : pList) {
            List<Pack> cList = new ArrayList();
            String x = "-" + p.getName() + p.getVersion();
            JSONArray curList = ini;
            JSONArray ncList = con; 
            if (!chkList(x, ncList)) {
                if(p.getConflicts() !=null) {
                    ncList.addAll(p.getConflicts());
                }
                cList.add(p);
                if(p.getDepends() != null && !chkDep(p, curList)) {
                    List<List<String>> lList = p.getDepends();
                    for(List l : lList) {
                        for(Object o : l) {
                            String s = o.toString();
                            if(!chkList(s, curList)) {
                                cList.addAll(search(sortPList(getPList(s, packRep))));
                            }
                            if(getPList(s, cList) != null) {
                                
                            }
                            
                        }
                    }
                }
                return cList;
            }
            break;
        }
        return cList;
    }
    
    public static Boolean chkDep(Pack p, JSONArray curList) {
        List<List<String>> lList = p.getDepends();
        Boolean ok = false;
        if(curList !=null && lList != null) {
            for(List l : lList) {
                for(Object o : l) {
                    String s = o.toString();
                    if(chkList(s, curList)) {
                        ok = true;
                        break;
                    }
                    return false;            
                }
            }
        }
        return ok;
    }
 

    //takes name and/or ver and returns pack object
    public static Pack getPack(String name, String ver) {
        if(ver != null) {
            for (Pack p : packRep) {
                if(name.contentEquals(p.getName()) && ver.compareTo(p.getVersion()) == 0) {
                    return p;
                }
            }
        }
        else {
            for (Pack p : packRep) {
                if(name.contentEquals(p.getName())) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public static Boolean chkList(JSONArray xlist, JSONArray list) {
        for(Object x : xlist){
            String xs = x.toString();
            List<Pack> pList = getPList(xs, packRep);
            for(Pack p : pList) {
                String ps = p.getName() + "=" + p.getVersion();
            for(Object o : list) {
                String s = o.toString();
                if(ps.contentEquals(s)) {
                    return true;
                }
            }
        }
        }
        return false;
    }
    
    //Takes String and returns boolean easy chk for ini and con list
    public static Boolean chkList(String x, JSONArray list) {
        for(Object o : list) {
            String s = o.toString();
            if(x.contentEquals(s)) {
                return true;
            }
        }
        return false;
    }
    //sort Pack list in order of cost
    public static List sortPList(List<Pack> pList) {
        List<Pack> xpList = new ArrayList();
        //pList = pList;
        int size = pList.size();
        for(int i = 0; i < size; i++) {
            int x = sortPListB(pList);
            xpList.add(pList.get(x));
            pList.remove(x);
        }
        return xpList;
    }
    // part 2
    public static int sortPListB(List<Pack> pList) {
        String size = pList.get(0).getSize();
        int index = 0;
        for(int i = 0; i < pList.size(); i++) {
            String xsize = pList.get(i).getSize();
            if(size.compareTo(xsize) < 0) {
                size = xsize;
                index = i;
            }
        }
        return index;
    }
    
    public static List getPList(String name, List<Pack> pList) {
        List<Pack> l = new ArrayList();
        String ver = "";
        String op = "";
        if(name.contains("<=")) {
            op = "<=";
            String[] parts = name.split("<=");
            name = parts[0];
            ver = parts[1];
        }
        
        else if(name.contains(">=")) {
            op = ">=";
            String[] parts = name.split(">=");
            name = parts[0];
            ver = parts[1];;
        }
                
        else if(name.contains("=")) {
            op = "=";
            String[] parts = name.split("=");
            name = parts[0];
            ver = parts[1];
        }
            
        else if(name.contains("<")) {
            op = "<";
            String[] parts = name.split("<");
            name = parts[0];
            ver = parts[1];
        }
            
        else if(name.contains(">")) {
            op = ">";
            String[] parts = name.split(">");
            name = parts[0];
            ver = parts[1];
        }
        else { 
            for(Pack s : pList) {
                if(name.contentEquals(s.getName())) {
                    l.add(s);
                }
            }
            return l;
        }
        
        if(op.contentEquals("=")) {
            for(Pack s : pList) {                
                if(name.contains(s.getName()) && ver.compareTo(s.getVersion()) == 0) {
                    l.add(s);
                }
            }
            return l;
        }
        if(op.contentEquals("<=")) {
            for(Pack s : pList) {                
                if(name.contains(s.getName()) && ver.compareTo(s.getVersion()) <= 0) {
                    l.add(s);
                }
            }
            return l;
        }
        if(op.contentEquals(">=")) {
            for(Pack s : pList) {                
                if(name.contains(s.getName()) && ver.compareTo(s.getVersion()) >= 0) {
                    l.add(s);
                }
            }
            return l;
        }
        if(op.contentEquals(">")) {
            for(Pack s : pList) {                
                if(name.contains(s.getName()) && ver.compareTo(s.getVersion()) > 0) {
                    l.add(s);
                }
            }
            return l;
        }
        if(op.contentEquals("<")) {
            for(Pack s : pList) {                
                if(name.contains(s.getName()) && ver.compareTo(s.getVersion()) < 0) {
                    l.add(s);
                }
            }
            return l;
        }
        return l;
    }
    
}
