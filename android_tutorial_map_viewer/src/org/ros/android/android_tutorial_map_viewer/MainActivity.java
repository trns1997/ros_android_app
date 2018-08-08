/*
 * Copyright (C) 2012 Google Inc.
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

package org.ros.android.android_tutorial_map_viewer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.CameraControlLayer;
import org.ros.android.view.visualization.layer.CameraControlListener;
import org.ros.android.view.visualization.layer.LaserScanLayer;
import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.OccupancyGridLayer;
import org.ros.android.view.visualization.layer.PathLayer;
import org.ros.android.view.visualization.layer.RobotLayer;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.time.NtpTimeProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import geometry_msgs.PoseStamped;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MainActivity extends RosActivity {

  private static final String MAP_FRAME = "map";
  private static final String ROBOT_FRAME = "base_link";

  private final SystemCommands systemCommands;

  private VisualizationView visualizationView;
  private ToggleButton followMeToggleButton;
  private CameraControlLayer cameraControlLayer;

  public MainActivity() {
    super("Map Viewer", "Map Viewer");
    systemCommands = new SystemCommands();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.main);
    visualizationView = (VisualizationView) findViewById(R.id.visualization);
    cameraControlLayer = new CameraControlLayer();
    visualizationView.onCreate(Lists.<Layer>newArrayList(cameraControlLayer,
        new OccupancyGridLayer("map"), new RobotLayer(ROBOT_FRAME), new PathLayer("/move_base/NavfnROS/plan")));
    followMeToggleButton = (ToggleButton) findViewById(R.id.follow_me_toggle_button);
    enableFollowMe();
  }

  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {
    visualizationView.init(nodeMainExecutor);
    cameraControlLayer.addListener(new CameraControlListener() {
      @Override
      public void onZoom(float focusX, float focusY, float factor) {
        disableFollowMe();
      }

      @Override
      public void onTranslate(float distanceX, float distanceY) {
        disableFollowMe();
      }

      @Override
      public void onRotate(float focusX, float focusY, double deltaAngle) {
        disableFollowMe();
      }

      @Override
      public void onDoubleTap(float x, float y) {
      }
    });
    NodeConfiguration nodeConfiguration =
        NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
            getMasterUri());
    NtpTimeProvider ntpTimeProvider =
        new NtpTimeProvider(InetAddressFactory.newFromHostString("192.168.0.1"),
            nodeMainExecutor.getScheduledExecutorService());
    ntpTimeProvider.startPeriodicUpdates(1, TimeUnit.MINUTES);
    nodeConfiguration.setTimeProvider(ntpTimeProvider);
    nodeMainExecutor.execute(visualizationView, nodeConfiguration);
    nodeMainExecutor.execute(systemCommands, nodeConfiguration);

    NodeMain node = new MovertoGoalNode();
    nodeMainExecutor.execute(node, nodeConfiguration);
  }

  public void onClearMapButtonClicked(View view) {
    toast("Clearing map...");
    systemCommands.reset();
    enableFollowMe();
  }

  public void onSaveMapButtonClicked(View view) {
    toast("Saving map...");
    systemCommands.saveGeotiff();
  }

  private void toast(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
      }
    });
  }

  public void onFollowMeToggleButtonClicked(View view) {
    boolean on = ((ToggleButton) view).isChecked();
    if (on) {
      enableFollowMe();
    } else {
      disableFollowMe();
    }
  }

  private void enableFollowMe() {
    Preconditions.checkNotNull(visualizationView);
    Preconditions.checkNotNull(followMeToggleButton);
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        visualizationView.getCamera().jumpToFrame(ROBOT_FRAME);
        followMeToggleButton.setChecked(true);
      }
    });
  }

  private void disableFollowMe() {
    Preconditions.checkNotNull(visualizationView);
    Preconditions.checkNotNull(followMeToggleButton);
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        visualizationView.getCamera().setFrame(MAP_FRAME);
        followMeToggleButton.setChecked(false);
      }
    });
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

      Button button = (Button) findViewById(R.id.button);
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
                          second.getPose().getOrientation().setW(0.530036454007);//0.102068293041);
                          second.getPose().getOrientation().setX(0);
                          second.getPose().getOrientation().setY(0);
                          second.getPose().getOrientation().setZ(-0.847974856599);//0.994777393971);
                          second.getPose().getPosition().setX(1.08480489254);//-1.03944051266
                          second.getPose().getPosition().setY(2.35004806519);//2.54983639717
                          second.getPose().getPosition().setZ(0);
                          publisher.publish(second);
                          break;

                        case "Pos2":
                          Log.i("mssg", "Pos2");
                          PoseStamped third = publisher.newMessage();
                          third.getHeader().setFrameId("map");
                          third.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                          third.getPose().getOrientation().setW(0.98022498029);//0.935369518692);
                          third.getPose().getOrientation().setX(0);
                          third.getPose().getOrientation().setY(0);
                          third.getPose().getOrientation().setZ(-0.197886300728);//-0.353671971609);
                          third.getPose().getPosition().setX(-0.701504588127);//-8.87926959991);
                          third.getPose().getPosition().setY(2.64645671844);//3.08588671684);
                          third.getPose().getPosition().setZ(0);
                          publisher.publish(third);
                          break;

                        case "Pos3":
                          Log.i("mssg", "Pos3");
                          PoseStamped fourth = publisher.newMessage();
                          fourth.getHeader().setFrameId("map");
                          fourth.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                          fourth.getPose().getOrientation().setW(0.633664274789);//-0.146961731995);
                          fourth.getPose().getOrientation().setX(0);
                          fourth.getPose().getOrientation().setY(0);
                          fourth.getPose().getOrientation().setZ(-0.773608161058);//0.989142178521);
                          fourth.getPose().getPosition().setX(-0.339089989662);//-1.84649252892);
                          fourth.getPose().getPosition().setY(5.05151462555);//-1.1724768877);
                          fourth.getPose().getPosition().setZ(0);
                          publisher.publish(fourth);
                          break;

                        case "Pos4":
                          Log.i("mssg", "Pos4");
                          PoseStamped fifth = publisher.newMessage();
                          fifth.getHeader().setFrameId("map");
                          fifth.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                          fifth.getPose().getOrientation().setW(0.834614741906);//0.998230198425);
                          fifth.getPose().getOrientation().setX(0);
                          fifth.getPose().getOrientation().setY(0);
                          fifth.getPose().getOrientation().setZ(0.550834124391);//-0.05946823482);
                          fifth.getPose().getPosition().setX(-0.374120593071);//-9.52041339874);
                          fifth.getPose().getPosition().setY(1.05043375492);//-0.241153270006);
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
