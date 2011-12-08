package org.talend.mdm.webapp.browserecords.server.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.talend.mdm.webapp.browserecords.server.bizhelpers.ViewHelper;

import com.amalto.core.util.Messages;
import com.amalto.core.util.MessagesFactory;
import com.amalto.webapp.core.bean.Configuration;
import com.amalto.webapp.core.util.Util;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSPutItem;

/**
 * 
 * @author asaintguilhem
 * 
 * read excel and csv file
 */

@SuppressWarnings("serial")
public class UploadData extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UploadData.class);

    private static final Messages MESSAGES = MessagesFactory.getMessages(
            "org.talend.mdm.webapp.browserecords.client.i18n.BrowseRecordsMessages", UploadData.class.getClassLoader()); //$NON-NLS-1$

    public UploadData() {
        super();
    }

    public String getCurrentDataModel() throws Exception {
        Configuration config = Configuration.getConfiguration();
        return config.getModel();
    }

    public String getCurrentDataCluster() throws Exception {
        Configuration config = Configuration.getConfiguration();
        return config.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String concept = "";//$NON-NLS-1$
        String viewPK = ""; //$NON-NLS-1$
        String fileType = "";//$NON-NLS-1$
        String sep = ",";//$NON-NLS-1$
        String textDelimiter = "\"";//$NON-NLS-1$
        String language = "en"; // default//$NON-NLS-1$
        String encoding = "utf-8";//$NON-NLS-1$
        String header = ""; //$NON-NLS-1$
        String mandatoryField = "";
        boolean cusExceptionFlag = false;

        boolean headersOnFirstLine = false;
        int lineNum = 0;
        response.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
        PrintWriter writer = response.getWriter();

        request.setCharacterEncoding("UTF-8");//$NON-NLS-1$
        
        try {
            if (!FileUploadBase.isMultipartContent(request)) {
                throw new ServletException(MESSAGES.getMessage("error_upload"));//$NON-NLS-1$
            }
            // Create a new file upload handler
            DiskFileUpload upload = new DiskFileUpload();

            // Set upload parameters
            upload.setSizeThreshold(0);
            upload.setSizeMax(-1);

            // Parse the request
            List items; // FileItem

            items = upload.parseRequest(request);

            String path = "/tmp/";//$NON-NLS-1$
            if (System.getProperty("os.name").toLowerCase().toLowerCase().matches(".*windows.*"))//$NON-NLS-1$//$NON-NLS-2$
                path = "c:/tmp/";//$NON-NLS-1$

            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMyy-HHmmssSSS"); //$NON-NLS-1$     
            String fileId = sd.format(new Date(System.currentTimeMillis()));

            File file = null;
            // Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                // FIXME: should handle more than files in parts e.g. text passed as parameter
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    // we are not expecting any field just (one) file(s)
                    String name = item.getFieldName();
                    LOG.debug("doPost() Field: '" + name + "' - value:'" + item.getString() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    if (name.equals("concept"))//$NON-NLS-1$
                        viewPK = item.getString();
                    if (name.equals("fileType"))//$NON-NLS-1$
                        fileType = item.getString();
                    if (name.equals("sep"))//$NON-NLS-1$
                        sep = item.getString();
                    if (name.equals("delimiter"))//$NON-NLS-1$
                        textDelimiter = item.getString();
                    if (name.equals("language"))//$NON-NLS-1$
                        language = item.getString();
                    if (name.equals("encodings"))//$NON-NLS-1$
                        encoding = item.getString();
                    if (name.equals("header"))//$NON-NLS-1$
                        header = item.getString();
                    if (name.equals("mandatoryField"))//$NON-NLS-1$
                        mandatoryField = item.getString();
                    if (name.equals("headersOnFirstLine"))//$NON-NLS-1$
                        headersOnFirstLine = "on".equals(item.getString());//$NON-NLS-1$
                } else {

                    file = File.createTempFile("upload", "tmp");//$NON-NLS-1$//$NON-NLS-2$
                    LOG.debug("doPost() data uploaded in " + file.getAbsolutePath()); //$NON-NLS-1$
                    file.deleteOnExit();
                    item.write(file);
                }// if field
            }// while item

            Locale locale = new Locale(language);
            concept = ViewHelper.getConceptFromDefaultViewName(viewPK);
            String[] fields = header.split("@"); //$NON-NLS-1$
            Set<String> mandatorySet = chechMandatoryField(mandatoryField, fields);
            
            if(mandatorySet.size() > 0){
                cusExceptionFlag = true;
                throw new ServletException(MESSAGES.getMessage(locale, "error_missing_mandatory_field")); //$NON-NLS-1$
            }

            if ("excel".equals(fileType.toLowerCase())) {//$NON-NLS-1$
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
                HSSFWorkbook wb = new HSSFWorkbook(fs);
                HSSFSheet sh = wb.getSheetAt(0);
                Iterator it = sh.rowIterator();

                while (it.hasNext()) {
                    HSSFRow row = (HSSFRow) it.next();
                    int count = row.getPhysicalNumberOfCells();
                    if (fields.length != count) {
                        cusExceptionFlag = true;
                        throw new ServletException(MESSAGES.getMessage(locale, "error_column_width")); //$NON-NLS-1$
                    }

                    ++lineNum;
                    if (lineNum == 1 && headersOnFirstLine)
                        continue;
                    StringBuffer xml = new StringBuffer();
                    boolean allCellsEmpty = true;
                    xml.append("<" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    for (int i = 0; i < fields.length; i++) {
                        HSSFCell tmpCell = row.getCell((short) i);                        
                        if (tmpCell != null) {
                            xml.append("<" + fields[i] + ">");//$NON-NLS-1$//$NON-NLS-2$
                            int cellType = tmpCell.getCellType();
                            String cellValue = "";//$NON-NLS-1$
                            switch (cellType) {
                                case HSSFCell.CELL_TYPE_NUMERIC: {
                                    double tmp = tmpCell.getNumericCellValue();
                                    cellValue = getStringRepresentation(tmp);
                                    break;
                                }
                                case HSSFCell.CELL_TYPE_STRING: {
                                    cellValue = tmpCell.getRichStringCellValue().getString();
                                    break;
                                }
                                case HSSFCell.CELL_TYPE_BOOLEAN: {
                                    boolean tmp = tmpCell.getBooleanCellValue();
                                    if (tmp)
                                        cellValue = "true";//$NON-NLS-1$
                                    else
                                        cellValue = "false";//$NON-NLS-1$
                                    break;
                                }
                                case HSSFCell.CELL_TYPE_FORMULA: {
                                    cellValue = tmpCell.getCellFormula();
                                    break;
                                }
                                case HSSFCell.CELL_TYPE_ERROR: {
                                    break;
                                }
                                case HSSFCell.CELL_TYPE_BLANK: {
                                }
                                default: {
                                }
                            }
                            
                            if (cellValue != null && !"".equals(cellValue))//$NON-NLS-1$
                                allCellsEmpty = false;

                            xml.append(StringEscapeUtils.escapeXml(cellValue));
                            xml.append("</" + fields[i] + ">");//$NON-NLS-1$//$NON-NLS-2$
                        }else{
                            xml.append("<" + fields[i] + "/>"); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }
                    
                    xml.append("</" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    // put document (except empty lines)
                    if (!allCellsEmpty)
                        putDocument(xml.toString(), language);
                }

            } else if ("csv".equals(fileType.toLowerCase())) { //$NON-NLS-1$
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));//$NON-NLS-1$
                while ((line = br.readLine()) != null) {
                    if (++lineNum == 1 && headersOnFirstLine)
                        continue;
                    StringBuffer xml = new StringBuffer();
                    xml.append("<" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    String separator = ",";//$NON-NLS-1$
                    if ("semicolon".equals(sep))//$NON-NLS-1$
                        separator = ";";//$NON-NLS-1$
                    String[] splits = line.split(separator);
                    if (fields.length != splits.length) {
                        cusExceptionFlag = true;
                        throw new ServletException(MESSAGES.getMessage(locale, "error_column_width")); //$NON-NLS-1$
                    }
                    // rebuild the values by checking delimiters
                    ArrayList<String> values = new ArrayList<String>();
                    if (textDelimiter == null || "".equals(textDelimiter.trim())) {//$NON-NLS-1$
                        values.addAll(Arrays.asList(splits));
                    } else {
                        String currentText = "";//$NON-NLS-1$
                        boolean textOpened = false;
                        for (int j = 0; j < splits.length; j++) {
                            if (splits[j].startsWith(textDelimiter)) {
                                if (splits[j].endsWith(textDelimiter)) {
                                    // we have a full text
                                    values.add(splits[j].substring(textDelimiter.length(),
                                            splits[j].length() - textDelimiter.length()));
                                } else {
                                    // we have the beginning of a text
                                    textOpened = true;
                                    currentText += splits[j].substring(textDelimiter.length());
                                }
                            } else {
                                if (splits[j].endsWith(textDelimiter) && !splits[j].endsWith("\\" + textDelimiter)) {//$NON-NLS-1$
                                    // we are finishing a text
                                    currentText += separator
                                            + splits[j].substring(0, splits[j].length() - textDelimiter.length());
                                    values.add(currentText);
                                    currentText = "";//$NON-NLS-1$
                                    textOpened = false;
                                } else {
                                    if (textOpened) {
                                        // the continuation of a text
                                        currentText += separator + splits[j];
                                    } else {
                                        // a number or not delimited string
                                        values.add(splits[j]);
                                    }
                                }
                            }
                        }
                    }
                    // build xml
                    if (values.size() > 0) {
                        for (int j = 0; j < fields.length; j++) {
                            xml.append("<" + fields[j] + ">");//$NON-NLS-1$//$NON-NLS-2$
                            if (j < values.size())
                                xml.append(StringEscapeUtils.escapeXml(values.get(j)));
                            xml.append("</" + fields[j] + ">");//$NON-NLS-1$//$NON-NLS-2$
                        }
                    }
                    xml.append("</" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    LOG.debug("Added line " + lineNum);//$NON-NLS-1$
                    LOG.trace("--val:\n" + xml);//$NON-NLS-1$
                    // put document
                    putDocument(xml.toString(), language);
                }
            }
            writer.print("true");//$NON-NLS-1$
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            if (cusExceptionFlag) {
                writer.print(e.getMessage());
                throw (ServletException) e;
            } else {
                writer.print(MESSAGES.getMessage("error_import", lineNum, e.getClass().getName(), e//$NON-NLS-1$
                        .getLocalizedMessage()));
                throw new ServletException(MESSAGES.getMessage("error_import", lineNum, e.getClass().getName(), e//$NON-NLS-1$
                        .getLocalizedMessage()));
            }

        } finally {
            writer.close();
        }

    }

    private Set<String> chechMandatoryField(String mandatoryField, String[] fields){
        Set<String> fieldSet = new HashSet<String>();
        for(String field : fields)
            fieldSet.add(field);
        
        String[] mandatoryFields = mandatoryField.split("@"); //$NON-NLS-1$
        Set<String> mandatorySet = new HashSet<String>();
        for(String field : mandatoryFields)
            mandatorySet.add(field);
        
        for(String str : fields){
            if(mandatorySet.contains(str))
                mandatorySet.remove(str);
        }
        
        return mandatorySet;
    }
    
    private void putDocument(String xml, String language) throws ServletException {
        try {
            Util.getPort().putItem(
                    new WSPutItem(new WSDataClusterPK(this.getCurrentDataCluster()), xml.toString(), new WSDataModelPK(this
                            .getCurrentDataModel()), false));
        } catch (RemoteException e) {
            String err = MESSAGES.getMessage("save_fail", ""); //$NON-NLS-1$ //$NON-NLS-2$ 
            if (e.getMessage().indexOf("ERROR_3:") == 0) { //$NON-NLS-1$
                err = e.getMessage();
            }

            if (e.getMessage().indexOf("<msg/>") > -1) //$NON-NLS-1$
                err = MESSAGES.getMessage("save_validationrule_fail", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
            else if (e.getMessage().indexOf("<msg>") > -1) {//$NON-NLS-1$)
                if (e.getMessage().indexOf(language.toUpperCase() + ":") == -1) //$NON-NLS-1$
                    err = MESSAGES
                            .getMessage(
                                    "save_validationrule_fail", "", e.getMessage().replace("<msg>", "[" + language.toUpperCase() + ":").replace("</msg>", "]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
                else
                    err = e.getMessage();
            }

            throw new ServletException(err);
        } catch (Exception e) {
            throw new ServletException(e.getClass().getName() + ": " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    /*
     * Returns a string corresponding to the double value given in parameter Exponent is removed and "0" are added at
     * the end of the string if necessary This method is useful when you import long itemid that you don't want to see
     * modified by importation method.
     */
    private String getStringRepresentation(double value) {
        String result = ""; //$NON-NLS-1$

        result = Double.toString(value);

        int index = result.indexOf("E");//$NON-NLS-1$

        String base = result;

        if (index > 0) {
            try {
                base = result.substring(0, index);
                String puissance = result.substring(index + 1);

                int puissanceValue = Integer.parseInt(puissance);

                int indexPoint = base.indexOf(".");//$NON-NLS-1$

                if (indexPoint > 0) {
                    String beforePoint = base.substring(0, indexPoint);
                    String afterPoint = base.substring(indexPoint + 1);

                    if (puissanceValue >= afterPoint.length()) {
                        base = beforePoint + "" + afterPoint;//$NON-NLS-1$
                        puissanceValue -= afterPoint.length();
                    } else {
                        String newBeforePoint = beforePoint + "" + afterPoint.substring(0, puissanceValue);//$NON-NLS-1$
                        String newAfterPoint = afterPoint.substring(puissanceValue);
                        base = newBeforePoint + "." + newAfterPoint;//$NON-NLS-1$
                        puissanceValue = 0;
                    }
                }

                for (int j = 0; j < puissanceValue; j++) {
                    base += "0";//$NON-NLS-1$
                }

                result = base;

            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
}
