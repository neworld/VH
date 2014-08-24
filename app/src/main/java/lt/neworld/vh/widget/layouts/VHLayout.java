package lt.neworld.vh.widget.layouts;

import android.content.Context;
import android.graphics.Rect;

import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.core.Section;
import com.comcast.freeflow.layouts.FreeFlowLayoutBase;
import com.comcast.freeflow.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

public class VHLayout extends FreeFlowLayoutBase {

    private final int horizontalHeight;
    private final int verticalHeight;
    private final int squareHeight;

    private HashMap<Object, FreeFlowItem> map;
    private Section s;

    public VHLayout(Context context) {
        final float density = context.getResources().getDisplayMetrics().density;
        horizontalHeight = (int) (202 * density);
        verticalHeight = (int) (360 * density);
        squareHeight = (int) (180 * density);
    }

    @Override
    public HashMap<Object, FreeFlowItem> getItemProxies(int viewPortLeft, int viewPortTop) {

        Rect viewport = new Rect(
                viewPortLeft,
                viewPortTop,
                viewPortLeft + width,
                viewPortTop + height
        );

        HashMap<Object, FreeFlowItem> ret = new HashMap<Object, FreeFlowItem>();

        for (Map.Entry<Object, FreeFlowItem> pairs : map.entrySet()) {
            FreeFlowItem p = pairs.getValue();
            if (Rect.intersects(p.frame, viewport))
                ret.put(pairs.getKey(), p);
        }

        return ret;
    }

    @Override
    public void setLayoutParams(FreeFlowLayoutParams params) {

    }

    @Override
    public void prepareLayout() {
        map = new HashMap<Object, FreeFlowItem>();
        s = itemsAdapter.getSection(0);
        int rowIndex;
        for (int i = 0; i < s.getDataCount(); i++) {
            rowIndex = i / 4;

            FreeFlowItem p = new FreeFlowItem();
            p.isHeader = false;
            p.itemIndex = i;
            p.itemSection = 0;
            p.data = s.getDataAtIndex(i);

            Rect r = new Rect();

            final int top = rowIndex * (verticalHeight + horizontalHeight);

            switch (i % 4) {
                case (0):
                    r.left = 0;
                    r.top = top;
                    r.right = width;
                    r.bottom = top + horizontalHeight;

                    break;

                case (1):
                    r.left = 0;
                    r.right = width / 2;
                    r.top = top + horizontalHeight;
                    r.bottom = top + horizontalHeight + verticalHeight;

                    break;

                case (2):
                    r.left = width / 2;
                    r.right = width;
                    r.top = top + horizontalHeight;
                    r.bottom = top + horizontalHeight + squareHeight;

                    break;

                case (3):
                    r.left = width / 2;
                    r.right = width;
                    r.top = top + horizontalHeight + squareHeight;
                    r.bottom = top + horizontalHeight + 2 * squareHeight;
                    break;

                default:
                    break;
            }
            p.frame = r;
            map.put(s.getDataAtIndex(i), p);
        }
    }

    @Override
    public FreeFlowItem getFreeFlowItemForItem(Object item) {
        return map.get(item);
    }

    @Override
    public boolean horizontalScrollEnabled() {
        return false;
    }

    @Override
    public boolean verticalScrollEnabled() {
        return true;
    }

    @Override
    public int getContentWidth() {
        return 0;
    }

    @Override
    public int getContentHeight() {
        return s.getDataCount() / 4 * (horizontalHeight + verticalHeight);
    }

    @Override
    public FreeFlowItem getItemAt(float x, float y) {
        return ViewUtils.getItemAt(map, (int)x, (int)y);
    }
}
