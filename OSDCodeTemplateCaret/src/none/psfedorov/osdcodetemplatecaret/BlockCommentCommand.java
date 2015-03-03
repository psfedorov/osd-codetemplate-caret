package none.psfedorov.osdcodetemplatecaret;

import oracle.dbtools.worksheet.editor.Worksheet;

import oracle.ide.Ide;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.controller.Command;
import oracle.ide.extension.RegisteredByExtension;
import oracle.ide.view.View;

import oracle.javatools.editor.BasicEditorPane;

@RegisteredByExtension("none.psfedorov.osdcodetemplatecaret")
public class BlockCommentCommand extends Command {
    
    public static final String ACTION_ID = "none.psfedorov.osdcodetemplatecaret.BlockComment";
    
    public BlockCommentCommand() {
        super(Ide.findCmdID(ACTION_ID));
    }



    @Override
    public int doit() throws Exception {
        View view = getContext().getView();
        if (!(view instanceof Worksheet) && !(view instanceof CodeEditor)) {
            return OK;
        }
        BasicEditorPane pane = (view instanceof Worksheet) ? ((Worksheet) view).getFocusedEditorPane() : ((CodeEditor) view).getFocusedEditorPane();
        String selectedText = pane.getSelectedText();
        if (selectedText != null) {
            pane.replaceSelection((selectedText.startsWith("/*") && selectedText.endsWith("*/"))
                ? selectedText.substring(2, selectedText.length() - 2).replace("\\*", "/*").replace("*\\", "*/")
                : "/*" + selectedText.replace("/*", "\\*").replace("*/", "*\\") + "*/"
            );
        }
        return OK;
    }
}
