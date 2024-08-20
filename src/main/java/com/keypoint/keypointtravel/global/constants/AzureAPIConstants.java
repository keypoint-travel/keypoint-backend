package com.keypoint.keypointtravel.global.constants;

public class AzureAPIConstants {

    public static final String ENDPOINT = "https://keypoint-travel-api.cognitiveservices.azure.com";
    public static final String AZURE_KEY_HEADER = "Ocp-Apim-Subscription-Key";
    public static final String API_VERSION = "2023-07-31";
    private static final String RECEIPT_MODEL_ID = "prebuilt-receipt";
    public static final String REQUEST_ANALYSIS =
        "/formrecognizer/documentModels/" + RECEIPT_MODEL_ID + ":analyze?api-version=" + API_VERSION
            + "&stringIndexType=utf16CodeUnit";
}
