/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_pubsub;

import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.rosjava_tutorial_pubsub.Talker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import geometry_msgs.PoseStamped;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.sample.models.SampleModel;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    int index = 999;
    HashMap<String, Integer> wifi_info = new HashMap<>();
    HashMap<String, Integer> rp0 = new HashMap<>();
    HashMap<String, Integer> rp1 = new HashMap<>();
    HashMap<String, Integer> rp2 = new HashMap<>();
    HashMap<String, Integer> rp3 = new HashMap<>();
    HashMap<String, Integer> rp4 = new HashMap<>();
    HashMap<String, Integer> rp5 = new HashMap<>();
    HashMap<String, Integer> rp6 = new HashMap<>();
    HashMap<String, Integer> rp7 = new HashMap<>();
    HashMap<String, Integer> rp8 = new HashMap<>();
    HashMap<String, Integer> rp9 = new HashMap<>();
    HashMap<String, Integer> rp10 = new HashMap<>();
    HashMap<String, Integer> rp11 = new HashMap<>();
    HashMap<String, Integer> rp12 = new HashMap<>();

    List<String> rp0_bssids = Arrays.asList("f4:0f:1b:97:e1:30", "f4:0f:1b:97:fd:30");
    List<Integer> rp0_rssi = Arrays.asList(-51, -63);

    List<String> rp1_bssids = Arrays.asList("18:64:72:22:6b:61", "18:64:72:22:6c:e1", "18:64:72:22:24:c1", "18:64:72:22:68:81", "18:64:72:22:c1:21", "18:64:72:22:b6:81", "18:64:72:22:4f:c1", "18:64:72:22:6b:81");
    List<Integer> rp1_rssi = Arrays.asList(-49, -50, -58, -57, -61, -59, -60, -64);

    List<String> rp2_bssids = Arrays.asList("18:64:72:22:6b:61", "18:64:72:22:6c:e1", "18:64:72:22:24:c1", "18:64:72:22:c1:21", "18:64:72:22:6b:81", "18:64:72:22:c2:41", "18:64:72:22:68:81");
    List<Integer> rp2_rssi = Arrays.asList(-53, -55, -53, -57, -61, -60, -53);

    List<String> rp3_bssids = Arrays.asList("18:64:72:22:c1:21", "18:64:72:22:68:81", "18:64:72:22:6b:61", "18:64:72:22:c2:41", "18:64:72:22:24:c1", "18:64:72:22:6c:e1", "18:64:72:22:6b:81", "18:64:72:22:aa:01");
    List<Integer> rp3_rssi = Arrays.asList(-53, -41, -56, -60, -57, -61, -64, -65);

    List<String> rp4_bssids = Arrays.asList("00:25:84:86:7b:90", "00:25:84:23:1d:90");
    List<Integer> rp4_rssi = Arrays.asList(-44, -55);

    List<String> rp5_bssids = Arrays.asList();
    List<Integer> rp5_rssi = Arrays.asList();

    List<String> rp6_bssids = Arrays.asList();
    List<Integer> rp6_rssi = Arrays.asList();

    List<String> rp7_bssids = Arrays.asList();
    List<Integer> rp7_rssi = Arrays.asList();

    List<String> rp8_bssids = Arrays.asList();
    List<Integer> rp8_rssi = Arrays.asList();

    List<String> rp9_bssids = Arrays.asList();
    List<Integer> rp9_rssi = Arrays.asList();

    List<String> rp10_bssids = Arrays.asList();
    List<Integer> rp10_rssi = Arrays.asList();

    List<String> rp11_bssids = Arrays.asList();
    List<Integer> rp11_rssi = Arrays.asList();


    public MainActivity() {
        super("RosAndroidExample", "RosAndroidExample");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateLoc();
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeMain node = new MovertoGoalNode();

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeMainExecutor.execute(node, nodeConfiguration);
    }

    public class MovertoGoalNode extends AbstractNodeMain implements NodeMain {
        @Override
        public GraphName getDefaultNodeName() {
            return GraphName.of("move_base_simple/goal");
        }

        private ArrayList<SampleModel> createSampleData() {
            ArrayList<SampleModel> items = new ArrayList<>();
            items.add(new SampleModel("Start"));
            items.add(new SampleModel("Pos1"));
            items.add(new SampleModel("Pos2"));
            items.add(new SampleModel("Pos3"));
            items.add(new SampleModel("Pos4"));
            items.add(new SampleModel("Lorem ipsum"));
            items.add(new SampleModel("Dolor sit"));
            items.add(new SampleModel("Some random word"));
            items.add(new SampleModel("guess who's back"));
            return items;
        }

        @Override
        public void onStart(ConnectedNode connectedNode) {
            final Publisher<PoseStamped> publisher =
                    connectedNode.newPublisher("move_base_simple/goal", "geometry_msgs/PoseStamped");

            Button take = findViewById(R.id.button);
            Button call = findViewById(R.id.call);
            take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleSearchDialogCompat<SampleModel> dialog = new SimpleSearchDialogCompat<>(MainActivity.this, "Search...",
                            "What are you looking for...?", null, createSampleData(),
                            new SearchResultListener<SampleModel>() {
                                @Override
                                public void onSelected(BaseSearchDialogCompat dialog,
                                                       SampleModel item, int position) {
                                    Toast.makeText(MainActivity.this, item.getTitle(),
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                    switch (item.getTitle()) {
                                        case "Start":
                                            Log.i("mssg", "Start");
                                            PoseStamped pose = publisher.newMessage();
                                            pose.getHeader().setFrameId("map");
                                            pose.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                            pose.getPose().getOrientation().setW(0.99999809223);
                                            pose.getPose().getOrientation().setX(0);
                                            pose.getPose().getOrientation().setY(0);
                                            pose.getPose().getOrientation().setZ(0.00195333988248);
                                            pose.getPose().getPosition().setX(0);
                                            pose.getPose().getPosition().setY(0);
                                            pose.getPose().getPosition().setZ(0);
                                            publisher.publish(pose);
                                            break;

                                        case "Pos1":
                                            Log.i("mssg", "Pos1");
                                            PoseStamped second = publisher.newMessage();
                                            second.getHeader().setFrameId("map");
                                            second.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                            second.getPose().getOrientation().setW(0.102068293041);
                                            second.getPose().getOrientation().setX(0);
                                            second.getPose().getOrientation().setY(0);
                                            second.getPose().getOrientation().setZ(0.994777393971);
                                            second.getPose().getPosition().setX(-1.03944051266);
                                            second.getPose().getPosition().setY(2.54983639717);
                                            second.getPose().getPosition().setZ(0);
                                            publisher.publish(second);
                                            break;

                                        case "Pos2":
                                            Log.i("mssg", "Pos2");
                                            PoseStamped third = publisher.newMessage();
                                            third.getHeader().setFrameId("map");
                                            third.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                            third.getPose().getOrientation().setW(0.935369518692);
                                            third.getPose().getOrientation().setX(0);
                                            third.getPose().getOrientation().setY(0);
                                            third.getPose().getOrientation().setZ(-0.353671971609);
                                            third.getPose().getPosition().setX(-8.87926959991);
                                            third.getPose().getPosition().setY(3.08588671684);
                                            third.getPose().getPosition().setZ(0);
                                            publisher.publish(third);
                                            break;

                                        case "Pos3":
                                            Log.i("mssg", "Pos3");
                                            PoseStamped fourth = publisher.newMessage();
                                            fourth.getHeader().setFrameId("map");
                                            fourth.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                            fourth.getPose().getOrientation().setW(-0.146961731995);
                                            fourth.getPose().getOrientation().setX(0);
                                            fourth.getPose().getOrientation().setY(0);
                                            fourth.getPose().getOrientation().setZ(0.989142178521);
                                            fourth.getPose().getPosition().setX(-1.84649252892);
                                            fourth.getPose().getPosition().setY(-1.1724768877);
                                            fourth.getPose().getPosition().setZ(0);
                                            publisher.publish(fourth);
                                            break;

                                        case "Pos4":
                                            Log.i("mssg", "Pos4");
                                            PoseStamped fifth = publisher.newMessage();
                                            fifth.getHeader().setFrameId("map");
                                            fifth.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                            fifth.getPose().getOrientation().setW(0.998230198425);
                                            fifth.getPose().getOrientation().setX(0);
                                            fifth.getPose().getOrientation().setY(0);
                                            fifth.getPose().getOrientation().setZ(-0.05946823482);
                                            fifth.getPose().getPosition().setX(-9.52041339874);
                                            fifth.getPose().getPosition().setY(-0.241153270006);
                                            fifth.getPose().getPosition().setZ(0);
                                            publisher.publish(fifth);
                                            break;
                                    }
                                }
                            });
                    dialog.show();
                    dialog.getSearchBox().setTypeface(Typeface.SERIF);
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("check", String.valueOf(index));
                    switch (index) {
                        case 0:
                            PoseStamped third = publisher.newMessage();
                            third.getHeader().setFrameId("map");
                            third.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                            third.getPose().getOrientation().setW(0.125697062089);
                            third.getPose().getOrientation().setX(0);
                            third.getPose().getOrientation().setY(0);
                            third.getPose().getOrientation().setZ(0.992068671304);
                            third.getPose().getPosition().setX(-1.34180116653);
                            third.getPose().getPosition().setY(2.47653889656);
                            third.getPose().getPosition().setZ(0);
                            publisher.publish(third);
                            break;

                        case 1:
                            PoseStamped second = publisher.newMessage();
                            second.getHeader().setFrameId("map");
                            second.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                            second.getPose().getOrientation().setW(0.994343749711);
                            second.getPose().getOrientation().setX(0);
                            second.getPose().getOrientation().setY(0);
                            second.getPose().getOrientation().setZ(-0.106209733121);
                            second.getPose().getPosition().setX(-9.19212150574);
                            second.getPose().getPosition().setY(-0.389170885086);
                            second.getPose().getPosition().setZ(0);
                            publisher.publish(second);
                            break;
                    }

                }
            });
        }
    }

    public static double comparePoints(HashMap<String, Integer> p1, HashMap<String, Integer> p2) {
        double sum = 0;
        double cnt = 0;
        double ratio;
        for (String list : p1.keySet()) {
            Integer p2Val = p2.get(list);
            if (p2Val == null) {
                continue;
            }
            double diff = p1.get(list) - p2Val;
            sum += diff * diff;
            cnt++;
        }
        if (sum == 0) {
            sum = 999;
        }
        if (cnt == 0) {
            ratio = 0;
        } else {
            ratio = p1.size() / cnt;
        }
//        Log.i("check", ratio + ";       " + cnt + ";        " + p1.size() + ";        " + p2.size());
        return (Math.sqrt(sum) / ratio);
    }

    public HashMap<String, Integer> make_rp(List id, List rssi) {
        HashMap<String, Integer> rp = new HashMap<>();
        for (int i = 0; i < id.size(); i++) {
            rp.put(String.valueOf(id.get(i)), (Integer) rssi.get(i));
        }
        return rp;
    }

    public void updateLoc() {

        final List<Double> euciledian = new ArrayList<>();
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                assert wifiManager != null;

                List<HashMap> refPoint = new ArrayList<>();
                List<Double> euciledianAvg;

                rp0 = make_rp(rp0_bssids, rp0_rssi);
                rp1 = make_rp(rp1_bssids, rp1_rssi);
                rp2 = make_rp(rp2_bssids, rp2_rssi);
                rp3 = make_rp(rp3_bssids, rp3_rssi);
                rp4 = make_rp(rp4_bssids, rp4_rssi);
                rp5 = make_rp(rp5_bssids, rp5_rssi);
                rp6 = make_rp(rp6_bssids, rp6_rssi);
                rp7 = make_rp(rp7_bssids, rp7_rssi);
                rp8 = make_rp(rp8_bssids, rp8_rssi);
                rp9 = make_rp(rp9_bssids, rp9_rssi);

                refPoint.add(rp0);
                refPoint.add(rp1);
                refPoint.add(rp2);
                refPoint.add(rp3);
                refPoint.add(rp4);
                refPoint.add(rp5);
                refPoint.add(rp6);
                refPoint.add(rp7);
                refPoint.add(rp8);
                refPoint.add(rp9);

                TextView location = findViewById(R.id.location);
                wifiManager.startScan();
                List<ScanResult> scanRef = wifiManager.getScanResults();

                for (ScanResult result : scanRef) {
                    if (result.level < -65 || !result.SSID.equals("eduroam")) {
                    } else {
                        wifi_info.put(result.BSSID, result.level);
                    }
                }

                for (HashMap element : refPoint) {
                    double compare_res = comparePoints(element, wifi_info);
                    euciledian.add(compare_res);
                }
//                Log.i("loc", String.valueOf(euciledian));

                if (euciledian.size() >= (refPoint.size()) * 8) {
                    euciledianAvg = rolling(refPoint, euciledian);

                    Log.i("loc", String.valueOf(euciledianAvg));

                    Double min = POSITIVE_INFINITY;
                    for (int i = 0; i < euciledianAvg.size(); i++) {
                        if (min > euciledianAvg.get(i)) {
                            min = euciledianAvg.get(i);
                            index = i;
                        }
                    }
                    location.setText(String.valueOf(index));
                    Log.i("loc", String.valueOf(index));
                    euciledian.clear();
                }
                handler.postDelayed(this, 75);
            }
        };

        handler.post(runnableCode);

    }

    public List<Double> rolling(List<HashMap> ref, List<Double> euc) {
        List<Double> avgList = new ArrayList<>();
        for (int i = 0; i < ref.size(); i++) {
            double sum = 0;
            double avg = 0;
            int cnt = 0;
            for (int j = 0; j < euc.size(); j++) {
                if (j % ref.size() == i) {
                    sum += euc.get(j);
                    cnt++;
                }
            }
//            Log.i("check", String.valueOf(cnt));
            avg = sum / cnt;
            avgList.add(avg);
        }
        return avgList;
    }
}
