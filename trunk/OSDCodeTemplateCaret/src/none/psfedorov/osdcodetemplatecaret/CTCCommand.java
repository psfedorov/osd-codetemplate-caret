package none.psfedorov.osdcodetemplatecaret;



import javax.swing.text.Caret;

import oracle.dbtools.raptor.templates.CodeTemplateUtil;
import oracle.dbtools.worksheet.editor.Worksheet;

import oracle.ide.Ide;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.controller.Command;
import oracle.ide.extension.RegisteredByExtension;
import oracle.ide.view.View;

import oracle.javatools.editor.BasicEditorPane;



@RegisteredByExtension("none.psfedorov.osdcodetemplatecaret")
public class CTCCommand extends Command {

    public static final String ACTION_ID = "none.psfedorov.osdcodetemplatecaret.CTC";

    public static final String CURSOR_MARKER = "$cursor$";

    public CTCCommand() {
        super(Ide.findCmdID(ACTION_ID));
    }



    @Override
    public int doit() throws Exception {
        View view = getContext().getView();
        if (!(view instanceof Worksheet) && !(view instanceof CodeEditor)) {
            return OK;
        }
        BasicEditorPane pane = (view instanceof Worksheet) ? ((Worksheet) view).getFocusedEditorPane() : ((CodeEditor) view).getFocusedEditorPane();
        Caret caret = pane.getCaret();
        int offset = caret.getDot();
        int lineStartOffset = pane.getLineStartOffset(pane.getLineFromOffset(offset));
        String[] splitLineText = pane.getText(lineStartOffset, offset - lineStartOffset).split("\\W");
        String templateId = splitLineText[splitLineText.length - 1];
        String template = CodeTemplateUtil.getModel().get(templateId);
        if (template != null) {
            int templateIdLength = templateId.length();
            int insertOffset = offset - templateIdLength;
            caret.setDot(insertOffset);
            caret.moveDot(insertOffset + templateIdLength);
            pane.replaceSelection(template.replace(CURSOR_MARKER, ""));
            int cursorMarkerPosition = template.indexOf(CURSOR_MARKER);
            if (cursorMarkerPosition >= 0) {
                caret.setDot(insertOffset + cursorMarkerPosition);
            }
        }
        return OK;
    }
}
