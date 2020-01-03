package cc.rome753.demo;

public class MainActivity extends BaseActivity {

    @Override
    protected void init() {
        setContentView(R.layout.activity_base);
        setTitle(getClass().getSimpleName());
        addCheckBoxes();
    }
}
