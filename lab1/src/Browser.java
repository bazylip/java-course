import DiskTree.*;

import java.io.File;

public class Browser {

    public static void main(String[] args) {
        File rootFile = new File(args[0]);
        try {
            if(args[1].equals("dict")) {
                AlphabeticalComparator cmp = new AlphabeticalComparator();
                DiskDirectory browser = new DiskDirectory(rootFile, cmp);
                browser.print();
            }else if(args[1].equals("size")){
                DiskDirectory browser = new DiskDirectory(rootFile);
                browser.sortBySize();
                browser.print();
            }else{
                throw new Exception("Invalid sorting argument");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
