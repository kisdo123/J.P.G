package capstone.android;

/**
 * Created by User on 2017-05-30.
 */
import android.util.Log;
import com.independentsoft.office.Unit;
import com.independentsoft.office.UnitType;
import com.independentsoft.office.drawing.Extents;
import com.independentsoft.office.drawing.Offset;
import com.independentsoft.office.drawing.Outline;
import com.independentsoft.office.drawing.PresetGeometry;
import com.independentsoft.office.drawing.RgbHexColor;
import com.independentsoft.office.drawing.ShapeLocking;
import com.independentsoft.office.drawing.ShapeType;
import com.independentsoft.office.drawing.SolidFill;
import com.independentsoft.office.drawing.TextAlignmentType;
import com.independentsoft.office.drawing.TextParagraph;
import com.independentsoft.office.drawing.TextRun;
import com.independentsoft.office.drawing.Transform2D;
import com.independentsoft.office.drawing.TransformGroup2D;
import com.independentsoft.office.presentation.CommonSlideData;
import com.independentsoft.office.presentation.NotesSlideSize;
import com.independentsoft.office.presentation.Presentation;
import com.independentsoft.office.presentation.Slide;
import com.independentsoft.office.presentation.SlideLayout;
import com.independentsoft.office.presentation.SlideMaster;
import com.independentsoft.office.presentation.SlideMasterTextStyles;
import com.independentsoft.office.presentation.SlideMasterTitleTextStyle;
import com.independentsoft.office.presentation.SlideSize;
import com.independentsoft.office.presentation.SlideSizeType;
import com.independentsoft.office.presentation.drawing.GroupShape;
import com.independentsoft.office.presentation.drawing.Placeholder;
import com.independentsoft.office.presentation.drawing.PlaceholderType;
import com.independentsoft.office.presentation.drawing.Shape;
import com.independentsoft.office.presentation.drawing.ShapeTextBody;

public class PptBuilder
{
    public void MakeSlideLayout(String text,String filePath)
    {
        try
        {
            Presentation presentation = new Presentation();

            GroupShape shapeTree = new GroupShape();
            shapeTree.setID("1");
            shapeTree.setName("ShapeTree");
            shapeTree.getShapeProperties().setTransformGroup2D(new TransformGroup2D());

            TextRun run1 = new TextRun(text);

            TextParagraph paragraph1 = new TextParagraph();
            paragraph1.setAlignment(TextAlignmentType.CENTER);
            paragraph1.getContent().add(run1);

            ShapeTextBody textBody = new ShapeTextBody();
            textBody.getParagraphs().add(paragraph1);
            textBody.getTextBodyProperties().setAutoFit(true);

            Shape shape1 = new Shape();
            shape1.setID("2");
            shape1.setName("Title 1");
            shape1.setLocking(new ShapeLocking());
            shape1.getLocking().setDisallowGrouping(true);
            shape1.setPlaceholder(new Placeholder(PlaceholderType.CENTERED_TITLE));
            shape1.setTextBody(textBody);

            Unit offsetX = new Unit(2, UnitType.INCH);
            Unit offsetY = new Unit(1, UnitType.INCH);

            Unit width = new Unit(6, UnitType.INCH);
            Unit height = new Unit(2, UnitType.INCH);

            RgbHexColor color = new RgbHexColor();
            color.setValue("FF0000"); //red

            SolidFill fill = new SolidFill();
            fill.setColorChoice(color);

            Outline outline = new Outline();
            outline.setLineWidth(new Unit(3, UnitType.PIXEL));
            outline.setSolidFill(fill);

            shape1.getShapeProperties().setTransform2D(new Transform2D());
            shape1.getShapeProperties().getTransform2D().setOffset(new Offset(offsetX, offsetY));
            shape1.getShapeProperties().getTransform2D().setExtents(new Extents(width, height));
            shape1.getShapeProperties().setPresetGeometry(new PresetGeometry(ShapeType.RECTANGLE));
            shape1.getShapeProperties().setOutline(outline);
            shape1.setTextBox(true);

            shapeTree.getElements().add(shape1);

            CommonSlideData commonSlideData = new CommonSlideData();
            commonSlideData.setShapeTree(shapeTree);

            SlideLayout layout1 = new SlideLayout();
            layout1.setCommonSlideData(getLayoutCommonSlideData());

            Slide slide1 = new Slide();
            slide1.setCommonSlideData(commonSlideData);
            slide1.setLayout(layout1);

            SlideMaster master1 = new SlideMaster();
            master1.setCommonSlideData(getLayoutCommonSlideData());
            master1.getLayouts().add(layout1);

            master1.setTextStyles(new SlideMasterTextStyles());
            master1.getTextStyles().setTitleStyle(new SlideMasterTitleTextStyle());
            master1.getTextStyles().getTitleStyle().getListLevel1TextStyle().getDefaultTextRunProperties().setFontSize(44);

            presentation.getSlides().add(slide1);
            presentation.getSlideMasters().add(master1);

            presentation.setSlideSize(new SlideSize(9144000, 6858000, SlideSizeType.SCREEN_4X3));
            presentation.setNotesSlideSize(new NotesSlideSize(6858000, 9144000));

            presentation.save(filePath+"/"+"output.pptx", true);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    static CommonSlideData getLayoutCommonSlideData()
    {
        GroupShape shapeTree = new GroupShape();
        shapeTree.setID("1");
        shapeTree.setName("layout");

        shapeTree.getShapeProperties().setTransformGroup2D(new TransformGroup2D());

        CommonSlideData commonSlideData = new CommonSlideData();
        commonSlideData.setShapeTree(shapeTree);

        return commonSlideData;
    }
}