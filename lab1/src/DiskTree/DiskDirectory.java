package DiskTree;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class DiskDirectory extends DiskElement implements Comparable<DiskElement>{
    private TreeSet<DiskElement> children;
    protected void print(int depth) {
        String lineToPrint = this.lineToPrint(depth, this.file);
        System.out.println(lineToPrint);
        for(DiskElement file : children){
            file.print(depth + 1);
        }
    }

    public void print(){
        this.print(0);
    }

    public DiskDirectory(File file) {
        this.file = file;
        this.children = new TreeSet();

        for(File f: this.file.listFiles()){
            if(f.isDirectory()){
                this.children.add(new DiskDirectory(f));
            }else{
                this.children.add(new DiskFile(f));
            }
        }
    }

    public DiskDirectory(File file, Comparator cmp){
        this.file = file;
        this.children = new TreeSet(cmp);

        for(File f: this.file.listFiles()){
            if(f.isDirectory()){
                this.children.add(new DiskDirectory(f, cmp));
            }else{
                this.children.add(new DiskFile(f));
            }
        }
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

    @Override protected long getSize(){
        return file.listFiles().length;
    }

    public void sortBySize(){
        for(DiskElement el: this.children){
            if (el instanceof DiskDirectory){
                ((DiskDirectory) el).sortBySize();
            }
        }
        TreeSet<DiskElement> sorted = (TreeSet<DiskElement>)this.children.descendingSet();
        this.children = sorted;
    }

}