/**
 * this interface exists just to allow the WebserviceHelper to make callbacks.
 */

package com.smt.sabkamaal.util;

public interface RequestReceiver {
	void requestFinished(String[] result) throws Exception;
}
