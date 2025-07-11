package com.arteriatech.ss.msec.iscan.v4.mutils.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.log.TraceLog;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.smp.client.odata.store.ODataRequestListener;
import com.sap.smp.client.odata.store.ODataResponse.Headers;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.util.List;
import java.util.Map;

public class OnlineRequestListener implements ODataRequestListener {
	
	private UIListener uiListener;
	private  String autoSync;
	private int operation;

	String entityType;
	String docKey;
	String collectionName;

	
	private final int SUCCESS = 0;
	private final int ERROR = -1;

	private Handler uiHandler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == SUCCESS) {
				// Notify the Activity the is complete
				String key = (String) msg.obj;
				TraceLog.d("requestsuccess - status message key" + key);
				try {
//					if(autoSync!=null && autoSync.equalsIgnoreCase("AutoSync")){
//						UpdatePendingRequests.getInstance().onRequestSuccess(operation, key);
//					}else{
						uiListener.onRequestSuccess(operation, key);
//					}

				} catch (ODataException e) {
					e.printStackTrace();
				} catch (OfflineODataStoreException e) {
					e.printStackTrace();
				}
			} else if (msg.what == ERROR) {
				Exception e = (Exception) msg.obj;
//				if(autoSync!=null && autoSync.equalsIgnoreCase("AutoSync")){
//					UpdatePendingRequests.getInstance().onRequestError(operation, e);
//				}else{
					uiListener.onRequestError(operation, e);
//				}

			}
		}
	};

	public OnlineRequestListener(int operation, UIListener uiListener, String collectionName,
								 String entityType, String docKey) {
		super();
		this.operation = operation;
		this.uiListener = uiListener;
		this.entityType = entityType;
		this.docKey = docKey;
		this.collectionName = collectionName;
	}

	public OnlineRequestListener(int operation, String autoSync) {
		super();
		this.operation = operation;
		this.autoSync = autoSync;
	}

	/*****************
	 * Methods that implements ODataRequestListener interface
	 *****************/

	@Override
	public void requestCacheResponse(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestCacheResponse");

		ODataProperty property;
		ODataPropMap properties;
		//Verify request’s response is not null. Request is always not null
		if (request.getResponse() != null) {
			//Parse the response
			ODataResponseSingle response = (ODataResponseSingle) request.getResponse();
			if (response != null) {
				//Get the response payload
				ODataEntitySet feed = (ODataEntitySet) response.getPayload();
				if (feed != null) {
					//Get the list of ODataEntity
					List<ODataEntity> entities = feed.getEntities();
					//Loop to retrieve the information from the response
					for (ODataEntity entity : entities) {
						//Obtain the properties you want to display in the screen
						properties = entity.getProperties();
						property = properties.get( "");
					}
					//TODO - Send content to the screen
				}
			}
		}

	}



	@Override
	public void requestFailed(ODataRequestExecution request, ODataException e) {
		TraceLog.scoped(this).d("requestFailed");
		if (request!=null && request.getResponse() !=null) {
			ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
			if (payload!=null && payload instanceof ODataError) {
				ODataError oError = (ODataError) payload;
				TraceLog.d("requestFailed - status message " + oError.getMessage());
				LogManager.writeLogError("Error :" + oError.getMessage());
				notifyErrorToListener(new OnlineODataStoreException(oError.getMessage()));

				return;
			} 
		} 
		notifyErrorToListener(e);
	}

	@Override
	public void requestFinished(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestFinished");
	}

	@Override
	public void requestServerResponse(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestServerResponse");
		if (request!=null && request.getResponse() !=null) {
			ODataResponseSingle response = (ODataResponseSingle) request.getResponse();
			Map<Headers, String> headerMap = response.getHeaders();
			String code  = headerMap.get(Headers.Code);
			TraceLog.d("requestServerResponse - status code " + code);
			String eTag = headerMap.get(Headers.ETag);
			if (!TextUtils.isEmpty(eTag)){
				notifySuccessToListener(eTag);
				return;
			} else {
				ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
				if (payload!=null && payload instanceof ODataEntity) {
					ODataEntity oEntity = (ODataEntity) payload;
					ODataPropMap properties = oEntity.getProperties();
					ODataProperty property=null;
//					if(oEntity.getEntityType().equalsIgnoreCase(Constants.InvoiceEntity)){
//
//						ODataNavigationProperty navProp = oEntity.getNavigationProperty(Constants.SSInvoiceItemDetails);
//						properties = oEntity.getProperties();
//						property=properties.get(Constants.InvoiceNo);
//						Constants.InvoiceNumber = property.getValue().toString();
//
//						property=properties.get(Constants.NetAmount);
//
//						if(property!=null){
//							BigDecimal mUnitPrice = (BigDecimal) property.getValue();
//							Constants.InvoiceTotalAmount = mUnitPrice.doubleValue();
//						}
//
//						property=properties.get(Constants.UnitPrice);
//
//						if(property!=null){
//							BigDecimal mUnitPrice = (BigDecimal) property.getValue();
//							Constants.MaterialUnitPrice = mUnitPrice.doubleValue();
//						}
//
//
//
//						if(navProp.getNavigationType().toString().equalsIgnoreCase("EntitySet")){
//							try {
//								ODataEntitySet feed = (ODataEntitySet) navProp.getNavigationContent();
//								List<ODataEntity> entities = feed.getEntities();
//								for (ODataEntity entity: entities) {
//									properties = entity.getProperties();
//									property=properties.get(Constants.UnitPrice);
//
//									if(property!=null){
//										BigDecimal mUnitPrice = (BigDecimal) property.getValue();
//										Constants.MaterialUnitPrice = mUnitPrice.doubleValue();
//									}
//
//									property=properties.get(Constants.NetAmount);
//
//									if(property!=null){
//										BigDecimal mGrossAmt = (BigDecimal) property.getValue();
//										Constants.MaterialNetAmount = mGrossAmt.doubleValue();
//									}
//
//
//								}
//							}catch (Exception e){
//								e.printStackTrace();
//							}
//						}
//
//					}else if(oEntity.getEntityType().equalsIgnoreCase(Constants.FinancialPostingsEntity)){
////						 property = properties.get("FIPGUID");
//						Constants.VisitActivityRefID = null;
//						properties = oEntity.getProperties();
//						property=properties.get(Constants.FIPDocNo);
//						Constants.FIPDocumentNumber = property.getValue().toString();
//						property=properties.get(Constants.FIPGUID);
//						Constants.VisitActivityRefID = (ODataGuid)property.getValue();
//
//						String popUpText = "Collection # "+Constants.FIPDocumentNumber+" created" ;
//
//						LogManager.writeLogInfo(popUpText);
//
//					}else if(oEntity.getEntityType().equalsIgnoreCase(Constants.FeedbackEntity)){
//						property = properties.get(Constants.FeebackGUID);
//
//						String popUpText = "Feedback created" ;
//
//						LogManager.writeLogInfo(popUpText);
//					}
//					else
					String docNo = "";
					if(oEntity.getEntityType().equalsIgnoreCase(entityType)){
						property = properties.get(docKey);
						try {
							docNo = property.getValue().toString();
						}
						catch (Exception e){
							e.printStackTrace();
						}

						String popUpText = collectionName ;
						if(!docNo.equalsIgnoreCase(""))
							popUpText.concat(" doc# " +docNo);
						if(operation == Operation.Create.getValue()) {
							popUpText.concat(" Created Successfully.");
						}
						else if(operation == Operation.Update.getValue()) {
							popUpText.concat(" Updated Successfully.");
						}else
							popUpText.concat(" Synchronized Successfully.");

						LogManager.writeLogInfo(popUpText);
					}

					notifySuccessToListener(docNo);
					return;
				}
			}
		}
		TraceLog.d("requestsuccess - status message before success");
		notifySuccessToListener(null);
	}

	@Override
	public void requestStarted(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestStarted");
	}

	
	
	/*****************
	 * Utils Methods
	 *****************/
		
	
	/**
     * Notify the OnlineUIListener that the request was successful.
     */
    protected void notifySuccessToListener(String key) {
        Message msg = uiHandler.obtainMessage();
        msg.what = SUCCESS;
        msg.obj = key;
        uiHandler.sendMessage(msg);
    }

    /**
     * Notify the OnlineUIListener that the request has an error.
     *
     * @param exception an Exception that denotes the error that occurred.
     */
    protected void notifyErrorToListener(Exception exception) {
        Message msg = uiHandler.obtainMessage();
        msg.what = ERROR;
        msg.obj = exception;
        uiHandler.sendMessage(msg);
        TraceLog.e("OnlineRequestListener::notifyError", exception);
    }

}
