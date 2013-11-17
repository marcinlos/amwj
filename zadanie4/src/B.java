public class B extends Second {

    @Override
    public int sort(int[] array) {
        System.out.println("I'm invoked directly");
        return super.sort(array);
    }

}
