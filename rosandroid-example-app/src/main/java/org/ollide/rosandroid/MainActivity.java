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

        private ArrayList<SampleModel> createSampleData(){
            ArrayList<SampleModel> items = new ArrayList<>();
            items.add(new SampleModel("First item"));
            items.add(new SampleModel("Second item"));
            items.add(new SampleModel("Third item"));
            items.add(new SampleModel("The ultimate item"));
            items.add(new SampleModel("Last item"));
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
                                            case "First item":
                                                Log.i("mssg", "First item");
                                                PoseStamped pose = publisher.newMessage();
                                                pose.getHeader().setFrameId("map");
                                                pose.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                                pose.getPose().getOrientation().setW(0.893834884065);
                                                pose.getPose().getOrientation().setX(0);
                                                pose.getPose().getOrientation().setY(0);
                                                pose.getPose().getOrientation().setZ(0.448396253362);
                                                pose.getPose().getPosition().setX(0.0512188374996);
                                                pose.getPose().getPosition().setY(-0.120340190828);
                                                pose.getPose().getPosition().setZ(0);
                                                publisher.publish(pose);
                                                break;
                                            case "Second item":
                                                Log.i("mssg", "Second item");
                                                PoseStamped second = publisher.newMessage();
                                                second.getHeader().setFrameId("map");
                                                second.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                                second.getPose().getOrientation().setW(0.79864677185);
                                                second.getPose().getOrientation().setX(0);
                                                second.getPose().getOrientation().setY(0);
                                                second.getPose().getOrientation().setZ(0.601800077944);
                                                second.getPose().getPosition().setX(0.235075980425);
                                                second.getPose().getPosition().setY(-2.96770429611);
                                                second.getPose().getPosition().setZ(0);
                                                publisher.publish(second);
                                                break;
                                            case "Third item":
                                                Log.i("mssg", "Third item");
                                                PoseStamped third = publisher.newMessage();
                                                third.getHeader().setFrameId("map");
                                                third.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                                                third.getPose().getOrientation().setW(0.642132484179);
                                                third.getPose().getOrientation().setX(0);
                                                third.getPose().getOrientation().setY(0);
                                                third.getPose().getOrientation().setZ(-0.76659368166);
                                                third.getPose().getPosition().setX(0);
                                                third.getPose().getPosition().setY(0);
                                                third.getPose().getPosition().setZ(0);
                                                publisher.publish(third);
                                                break;
                                        }
                                    }
                                });
                        dialog.show();
                        dialog.getSearchBox().setTypeface(Typeface.SERIF);
                    }
                });

//            final Button wall = findViewById(R.id.wall);
//            final Button trash = findViewById(R.id.trash);
//            final Button start = findViewById(R.id.start);
//            trash.setOnClickListener(new Vi   ew.OnClickListener() {
//                public void onClick(View v) {

//                }
//            });
//            wall.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {

//                }
//            });
//            start.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {

//                }
//            });


        }
    }

}