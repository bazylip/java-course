package DiskTree;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DiskElement {
    protected File file; //java.io.File;

    protected abstract void print(int depth);

    protected String getName(){
        return this.file.getName();
    }

    protected long getSize(){
        return this.file.length();
    }

    protected String lineToPrint(int depth, File file){
        Date date = new Date(file.lastModified());
        SimpleDateFormat template = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = template.format(date);
        String lineToPrint = new String();
        int nameColumn = 100;
        for(int i = 0; i < depth; i++) lineToPrint += '-';
        lineToPrint += file.getName();
        int currentLength = lineToPrint.length();
        for(int i = 0; i < nameColumn - currentLength; i++) lineToPrint += ' ';
        lineToPrint += file.isDirectory() ? "K " : "P ";
        lineToPrint += formattedDate;
        return lineToPrint;
    }

}