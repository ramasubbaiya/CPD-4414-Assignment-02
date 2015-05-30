/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 * Updated 2015 Mark Russell <mark.russell@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cpd4414.assign2;

import cpd4414.assign2.OrderQueue;
import cpd4414.assign2.Purchase;
import cpd4414.assign2.Order;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author c0652863
 */
public class OrderQueueTest {

    public OrderQueueTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWhenCustomerExistsAndPurchasesExistThenTimeReceivedIsNow() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(004, 4));
        order.addPurchase(new Purchase(006, 2));
        orderQueue.add(order);

        long expResult = new Date().getTime();
        long result = order.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }

    @Test
    public void testWhenCustomerIDAndCustomerNameNotExistThenThrowException() {
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("", "");
        try {
            orderQueue.add(ord);
        } catch (customerEmptyException e) {
            check = true;
        } catch (Exception e) {
            check = false;
        }
        assertTrue(check);
    }

    @Test
    public void testWhenListOfPurchasesDoesNotExistThenThrowException() throws Exception {
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00002", "XYZ");
        try {
            orderQueue.add(ord);
        } catch (listEmptyException e) {
            check = true;
        } catch (Exception e) {
            check = true;
        }
        assertTrue(check);
    }

    @Test
    public void testWhenThereAreOrdersInSystemThenReturnEarliestTimeRecievedOrder() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00003", "RST");
        ord.addPurchase(new Purchase(001, 4));
        ord.addPurchase(new Purchase(002, 2));
        orderQueue.add(ord);

        Order ord1 = new Order("CUST00004", "PDR");
        ord1.addPurchase(new Purchase(003, 5));
        ord1.addPurchase(new Purchase(004, 5));
        orderQueue.add(ord1);

        Order ord2 = new Order("CUST00005", "UVW");
        ord2.addPurchase(new Purchase(005, 5));
        ord2.addPurchase(new Purchase(0006, 5));
        orderQueue.add(ord2);

        assertEquals(ord, orderQueue.next());
    }

    @Test
    public void testWhenNoOrdersInSystemThenReturnNull() {
        OrderQueue queue = new OrderQueue();
        Order expectedResult = null;
        String result = "";
        try {
            queue.next();
        } catch (Exception e) {
            result = null;
        }
        assertEquals(expectedResult, result);
    }

    @Test
    public void testWhenOrderTimeRecievedNotSetThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00001", "DEF");
        ord.addPurchase(new Purchase(003, 4500));
        ord.addPurchase(new Purchase(002, 2500));
        boolean check = false;
        try {
            orderQueue.process(ord);
        } catch (noRecievedTimeException eq) {
            check = true;
        } catch (Exception e) {
            check = false;
        }
        assertTrue(check);
    }

    @Test
    public void testWhenOrderTimeRecievedAndOrderQuantityInInventoryThenSetTimeToNow() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00001", "DEF");
        ord.addPurchase(new Purchase(003, 4));
        ord.addPurchase(new Purchase(002, 2));
        orderQueue.add(ord);
        orderQueue.process(ord);
        Date expectedResult = new Date();
        Date result = ord.getTimeProcessed();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testWhenOrderDoesNotHaveQuantityInInventoryThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00001", "DEF");
        ord.addPurchase(new Purchase(003, 4500));
        ord.addPurchase(new Purchase(002, 2500));
        orderQueue.add(ord);
        boolean check = false;
        try {
            orderQueue.process(ord);
        } catch (noQuantityInInventoryException eq) {
            check = true;
        } catch (Exception e) {
            check = false;
        }
        assertTrue(check);
    }
    
    @Test
    public void testWhenOrderFulfilled() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00001", "ABC Construction");
        ord.addPurchase(new Purchase(4, 4));
        ord.addPurchase(new Purchase(6, 2));
        orderQueue.add(ord);
        orderQueue.process(ord);
        orderQueue.fulfill(ord);
        Date expectedResult = new Date();
        Date result = ord.getTimeFulfilled();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInFulfilledOrderDoesNotHaveATimeRecievedThenThrowException() throws Exception {
        testWhenOrderTimeRecievedNotSetThrowException();
    }
    
    @Test
    public void testInFulfilledOrderNotHaveQuantityInInventoryThenThrowException() throws Exception {
        testWhenOrderDoesNotHaveQuantityInInventoryThenThrowException();
    }
    
    @Test
    public void testInFulfilledOrderDoesNotHaveATimeProcessedThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order ord = new Order("CUST00001", "ABC Construction");
        ord.addPurchase(new Purchase(4, 4));
        ord.addPurchase(new Purchase(6, 2));
        orderQueue.add(ord);
        boolean check = false;
        try {
            orderQueue.fulfill(ord);
        } catch (noTimeProcessed eq) {
            check = true;
        } catch (Exception e) {
            check = true;
        }
        assertTrue(check);
    }
}
    
    