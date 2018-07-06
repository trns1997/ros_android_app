/*
 * Copyright (C) 2014 Oliver Degener.
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

package org.ollide.rosandroid;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;

import java.util.ArrayList;

import geometry_msgs.PoseStamped;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.sample.models.SampleModel;


public class MainActivity extends RosActivity {

    public MainActivity() {
        super("RosAndroidExample", "RosAndroidExample");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            final Publisher<geometry_msgs.PoseStamped> publisher =
                    connectedNode.newPublisher("move_base_simple/goal", "geometry_msgs/PoseStamped");

            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}