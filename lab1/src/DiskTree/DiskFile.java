package DiskTree;

import java.io.File;

public class DiskFile extends DiskElement implements Comparable<DiskElement>{

    protected void print(int depth){
        String lineToPrint = this.lineToPrint(depth, this.file);
        System.out.println(lineToPrint);
    }

    public DiskFile(File file) {
        this.file = file;
    }

    public int compareTo(DiskElement o){
        if (o.getSize() < this.getSize()){
            return 1;
        }else if (o.getSize() > this.getSize()){
            return -1;
        }else{
            return 0;
        }
    }

}
