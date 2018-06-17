public class Line {
    //记录开始点
    int start = 0;
    //记录结束点
    int end = 0;
    //记录长度
    int length = 0;

    public Line(int start, int end, int length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
}