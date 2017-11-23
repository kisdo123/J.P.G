package capstone.android;

import android.graphics.Bitmap;
import android.util.Log;

import com.independentsoft.office.Unit;
import com.independentsoft.office.UnitType;
import com.independentsoft.office.drawing.ComplexScriptFont;
import com.independentsoft.office.drawing.EastAsianFont;
import com.independentsoft.office.drawing.Extents;
import com.independentsoft.office.drawing.FillRectangle;
import com.independentsoft.office.drawing.Offset;
import com.independentsoft.office.drawing.PictureLocking;
import com.independentsoft.office.drawing.PresetGeometry;
import com.independentsoft.office.drawing.ShapeLocking;
import com.independentsoft.office.drawing.ShapeType;
import com.independentsoft.office.drawing.Stretch;
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
import com.independentsoft.office.presentation.drawing.Picture;
import com.independentsoft.office.presentation.drawing.Placeholder;
import com.independentsoft.office.presentation.drawing.PlaceholderType;
import com.independentsoft.office.presentation.drawing.Shape;
import com.independentsoft.office.presentation.drawing.ShapeTextBody;

import java.io.IOException;

import customized.customized.data.Storage;

public class PptBuilder {
    private Storage mStorage;

    PptBuilder(Storage storage){
        this.mStorage = storage;
    }

    public void Type1MakeSlide(String pptxPath) {
        Unit[] shpaeUnits = {new Unit(5,UnitType.INCH),new Unit(0,UnitType.INCH),new Unit(5,UnitType.INCH),new Unit(7,UnitType.INCH)};
        Unit[] pictureUnits = {new Unit(440, UnitType.PIXEL), new Unit(320, UnitType.PIXEL), new Unit(0, UnitType.INCH), new Unit(0, UnitType.INCH)};
        Presentation presentation = new Presentation();
        SlideLayout slideLayout = makeSlideLayout(1,"layout");
        SlideMaster slideMaster = makeSlideMaster(1,"layout",slideLayout);
        presentation.getSlideMasters().add(slideMaster);
        for(int j=0;j<this.mStorage.getSize();j++){
            try {
                GroupShape shapeTree1 = makeShapeTree(j, "ShapeTree"); // 프리젠테이션 내용 제작
                Shape shape = writeText1(this.mStorage.getOcrResultText(j), j, "Text"+j, shpaeUnits);
                Picture picture = writePicture1(this.mStorage.getAbsolutePath(j), j, "Title"+j, pictureUnits);
                shapeTree1.getElements().add(shape);
                shapeTree1.getElements().add(picture);
                CommonSlideData commonSlideData = new CommonSlideData();
                commonSlideData.setShapeTree(shapeTree1);
                Slide slide = makeSlide(commonSlideData);
                slide.setLayout(slideLayout);
                presentation.getSlides().add(slide);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        presentation.setSlideSize(new SlideSize(9144000, 6858000, SlideSizeType.SCREEN_4X3));
        presentation.setNotesSlideSize(new NotesSlideSize(6858000, 9144000));
        try {
            presentation.save(pptxPath, true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Type2MakeSlide() {
    }

    public void Type3MakeSlide() {

    }

    private Picture writePicture1(String filpath, int id, String Name, Unit[] units) throws IOException {
        //, byte[] filebuffer
        Picture picture = new Picture(filpath);
        //Picture picture = new Picture(fileName,filebuffer);
        picture.setID(String.valueOf(id));
        picture.setName(Name);
        picture.setStretch(new Stretch());
        picture.getStretch().setFillRectangle(new FillRectangle());
        picture.setLocking(new PictureLocking());
        picture.getLocking().setDisallowAspectRatioChange(true);
        picture.getShapeProperties().setPresetGeometry(new PresetGeometry(ShapeType.RECTANGLE));
        /*
        Unit pictureWidth = new Unit(640, UnitType.PIXEL);
        Unit pictureHeight = new Unit(480, UnitType.PIXEL);

        Unit pictureOffsetX = new Unit(2, UnitType.INCH);
        Unit pictureOffsetY = new Unit(2, UnitType.INCH);
        */
        Unit pictureWidth = units[0];
        Unit pictureHeight = units[1];
        Unit pictureOffsetX = units[2];
        Unit pictureOffsetY = units[3];

        Offset offset = new Offset(pictureOffsetX, pictureOffsetY);
        Extents extents = new Extents(pictureWidth, pictureHeight);

        picture.getShapeProperties().setTransform2D(new Transform2D());
        picture.getShapeProperties().getTransform2D().setOffset(offset);
        picture.getShapeProperties().getTransform2D().setExtents(extents);

        return picture;
    }

    private Shape writeText1(String string, int id, String Name, Unit[] units){

        TextRun run1 = new TextRun(string);
        Log.d("PptBuidler","type :"+run1.getComplexScriptFont());
        TextParagraph paragraph1 = new TextParagraph();
        paragraph1.setAlignment(TextAlignmentType.CENTER);
        paragraph1.getContent().add(run1);

        ShapeTextBody textBody = new ShapeTextBody();
        textBody.getParagraphs().add(paragraph1);

        Shape shape1 = new Shape();
        shape1.setID(String.valueOf(id));
        shape1.setName(Name);
        shape1.setLocking(new ShapeLocking());
        shape1.getLocking().setDisallowGrouping(true);
        shape1.setPlaceholder(new Placeholder(PlaceholderType.CENTERED_TITLE));
        shape1.setTextBody(textBody);

        /*
        Unit offsetX = new Unit(2, UnitType.INCH);
        Unit offsetY = new Unit(1, UnitType.INCH);

        Unit width = new Unit(6, UnitType.INCH);
        Unit height = new Unit(2, UnitType.INCH);
        */

        Unit offsetX = units[0];
        Unit offsetY = units[1];

        Unit width = units[2];
        Unit height = units[3];

        shape1.getShapeProperties().setTransform2D(new Transform2D());
        shape1.getShapeProperties().getTransform2D().setOffset(new Offset(offsetX, offsetY));
        shape1.getShapeProperties().getTransform2D().setExtents(new Extents(width, height));
        shape1.getShapeProperties().setPresetGeometry(new PresetGeometry(ShapeType.RECTANGLE));

        return shape1;
    }

    private CommonSlideData getLayoutCommonSlideData(int id, String Name){
        GroupShape shapeTree = new GroupShape();
        shapeTree.setID(String.valueOf(id));
        shapeTree.setName(Name);

        shapeTree.getShapeProperties().setTransformGroup2D(new TransformGroup2D());
        CommonSlideData commonSlideData = new CommonSlideData();
        commonSlideData.setShapeTree(shapeTree);
        return commonSlideData;
    }

    private GroupShape makeShapeTree(int id, String setName){
        GroupShape shapeTree = new GroupShape();
        shapeTree.setID(String.valueOf(id));
        shapeTree.setName(setName);
        shapeTree.getShapeProperties().setTransformGroup2D(new TransformGroup2D());
        return shapeTree;
    }

    private SlideMaster makeSlideMaster(int id, String setName,SlideLayout layout){
        SlideMaster slideMaster = new SlideMaster();
        slideMaster.setCommonSlideData(getLayoutCommonSlideData(id,setName));
        slideMaster.getLayouts().add(layout);
        slideMaster.setTextStyles(new SlideMasterTextStyles());
        slideMaster.getTextStyles().setTitleStyle(new SlideMasterTitleTextStyle());
        slideMaster.getTextStyles().getTitleStyle().getListLevel1TextStyle().getDefaultTextRunProperties().setFontSize(44);
        return slideMaster;
    }

    private Slide makeSlide(CommonSlideData commonSlideData){
        Slide slide = new Slide();
        slide.setCommonSlideData(commonSlideData);
        return slide;
    }

    private SlideLayout makeSlideLayout(int id, String setName){
        SlideLayout layout = new SlideLayout();
        layout.setCommonSlideData(getLayoutCommonSlideData(id,setName));
        return layout;
    }
}