package cn.app.peexam.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.app.peexam.R;
import cn.app.peexam.activity.ResultActivity.OnResponseProgramListener;
import cn.app.peexam.bean.Student;
import cn.app.peexam.util.NumberValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class ListSearchResultsAdapter extends Adapter<ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnResponseProgramListener programListener;
    private List<Student> studentList;
    private List<View> viewList = new ArrayList();

    public class ResultsHolder extends ViewHolder {
        @BindView(R.id.adapter_list_results_et_result)
        public EditText et_result;
        @BindView(R.id.adapter_list_results_et_result1)
        public EditText et_result1;
        private View itemView;
        @BindView(R.id.adapter_list_results_tv_sex)
        public TextView tv_Sex;
        @BindView(R.id.adapter_list_results_tv_stuInfo)
        public TextView tv_stuInfo;

        public ResultsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind((Object) this, itemView);
            this.et_result.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String temp = s.toString();
                    int posDot = temp.indexOf(".");
                    if (programListener.getProgram().getInputType() >= 2) {

                        if (programListener.getProgram().getUnit().contains("分") && posDot >= 0 && (temp.length() - posDot) - 1 > 1) {
                            String[] values = temp.split("\\.");
                            if (values.length == 2) {
                                if (Float.parseFloat(values[1]) >= 60 && Float.parseFloat(values[1]) < 100) {
                                    s.delete(posDot + 2, posDot + 3);
                                    return;
                                }
                            }
                        }

                        if (posDot >= 0 && (temp.length() - posDot) - 1 > 2) {
                            s.delete(posDot + 3, posDot + 4);
                        }
                    } else if (programListener.getProgram().getInputType() == 1) {
                        if (posDot >= 0 && (temp.length() - posDot) - 1 > 1) {
                            s.delete(posDot + 2, posDot + 3);
                        }
                    } else {
                        if (posDot >= 0 && (temp.length() - posDot) - 1 > 0) {
                            s.delete(posDot + 1, posDot + 2);
                        }
                    }
                }
            });
            this.et_result1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String temp = s.toString();
                    int posDot = temp.indexOf(".");
                    if (posDot > 0 && (temp.length() - posDot) - 1 > 1) {
                        s.delete(posDot + 2, posDot + 3);
                    }
                }
            });
        }

        @OnFocusChange({R.id.adapter_list_results_et_result})
        public void onFocusChanged(boolean hasFocus) {
            if (!hasFocus) {
                int position = ((Integer) this.itemView.getTag(R.string.tag)).intValue();
                String value = ((Student) ListSearchResultsAdapter.this.studentList.get(position)).getMark() == null ? "" : ((Student) ListSearchResultsAdapter.this.studentList.get(position)).getMark();
                if (this.et_result.getText().toString().trim().isEmpty()) {
                    this.et_result.setText(value);
                    return;
                }
                Toast toast = Toast.makeText(ListSearchResultsAdapter.this.context.getApplicationContext(), "数据格式不正确!", Toast.LENGTH_SHORT);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(ListSearchResultsAdapter.this.context.getResources().getDimension(R.dimen.x6));
                toast.setGravity(Gravity.CENTER, 0, 0);
                if (NumberValidationUtils.isDecimalNumber(this.et_result.getText().toString().trim())) {
                    try {
                        String textValue = this.et_result.getText().toString().trim();
                        int index = textValue.indexOf(".");
                        if (programListener.getProgram().getUnit().contains("分") && index > 0) {
                            int value1 = Integer.parseInt(textValue.substring(0, index));
//                            int value2 = Integer.parseInt(textValue.substring(index + 1, textValue.length()));
                            String value2 = textValue.substring(index + 1, textValue.length());
                            if (value2.length() == 1) {
                                this.et_result.setText(value1 + ".0" + value2);
                            }
                        }
                        float valueFloat = Float.parseFloat(this.et_result.getText().toString().trim());
                        if (ListSearchResultsAdapter.this.programListener.getProgram().getMin() == 0 && ListSearchResultsAdapter.this.programListener.getProgram().getMax() == 0) {
                            if (Float.parseFloat(this.et_result.getText().toString().trim()) < 0.0f) {
                                toast.setText("低于最小范围!");
                                toast.show();
                                this.et_result.setText(value);
                                return;
                            } else if ((this.et_result.getText().toString().startsWith("0") || this.et_result.getText().toString().startsWith(".")) && Float.parseFloat(this.et_result.getText().toString().trim()) == 0.0f) {
                                toast.setText("值不能为0!");
                                this.et_result.setText(value);
                                return;
                            } else {
                                return;
                            }
                        } else if (valueFloat < ((float) ListSearchResultsAdapter.this.programListener.getProgram().getMin())) {
                            toast.setText("低于最小范围!");
                            toast.show();
                            this.et_result.setText(value);
                            return;
                        } else if (valueFloat > ((float) ListSearchResultsAdapter.this.programListener.getProgram().getMax())) {
                            toast.setText("大于最大范围!");
                            toast.show();
                            this.et_result.setText(value);
                            return;
                        } else {
                            if (programListener.getProgram().getType() != 1) {
                                studentList.get(position).setMark(String.format("%." + programListener.getProgram().getInputType() + "f", valueFloat));
                            } else {
                                studentList.get(position).setMark(valueFloat + "/" + et_result1.getText().toString());
                            }
                            return;
                        }
                    } catch (Exception e) {
                        toast.setText("数据格式不正确!");
                        toast.show();
                        this.et_result.setText(value);
                        return;
                    }
                }
                toast.show();
                this.et_result.setText(value);
            }
        }


        @OnFocusChange({R.id.adapter_list_results_et_result1})
        public void onFocusChanged1(boolean hasFocus) {
            if (!hasFocus) {
                int position = ((Integer) this.itemView.getTag(R.string.tag)).intValue();
                String value = ((Student) ListSearchResultsAdapter.this.studentList.get(position)).getMark() == null ? "" : ((Student) ListSearchResultsAdapter.this.studentList.get(position)).getMark();
                if (value.contains("/")) {
                    value = value.split("/")[1];
                }
                if (this.et_result1.getText().toString().trim().isEmpty()) {
                    this.et_result1.setText(value);
                    return;
                }
                Toast toast = Toast.makeText(ListSearchResultsAdapter.this.context.getApplicationContext(), "数据格式不正确!", Toast.LENGTH_SHORT);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(ListSearchResultsAdapter.this.context.getResources().getDimension(R.dimen.x6));
                toast.setGravity(17, 0, 0);
                if (NumberValidationUtils.isDecimalNumber(this.et_result1.getText().toString().trim())) {
                    if (ListSearchResultsAdapter.this.programListener.getProgram().getMin() != 0 || ListSearchResultsAdapter.this.programListener.getProgram().getMax() == 0) {
                    }
                    try {
                        float valueFloat = Float.parseFloat(this.et_result1.getText().toString().trim());
                        if (ListSearchResultsAdapter.this.programListener.getProgram().getMin() == 0 && ListSearchResultsAdapter.this.programListener.getProgram().getMax() == 0) {
                            if (Float.parseFloat(this.et_result1.getText().toString().trim()) < 0.0f) {
                                toast.setText("数据格式不正确!");
                                toast.show();
                                this.et_result1.setText(value);
                                return;
                            } else if ((this.et_result1.getText().toString().startsWith("0") || this.et_result1.getText().toString().startsWith(".")) && Float.parseFloat(this.et_result1.getText().toString().trim()) == 0.0f) {
                                this.et_result1.setText(value);
                                toast.setText("值不能为0!");
                                return;
                            } else {
                                studentList.get(position).setMark(et_result.getText().toString() + "/" + valueFloat);
                                return;
                            }
                        } else if (valueFloat < ((float) ListSearchResultsAdapter.this.programListener.getProgram().getMin())) {
                            toast.setText("低于最小范围!");
                            toast.show();
                            this.et_result1.setText(value);
                            return;
                        } else if (valueFloat > ((float) ListSearchResultsAdapter.this.programListener.getProgram().getMax())) {
                            toast.setText("大于最大范围!");
                            toast.show();
                            this.et_result1.setText(value);
                            return;
                        } else {
                            studentList.get(position).setMark(et_result.getText().toString() + "/" + valueFloat);
                            return;
                        }
                    } catch (Exception e) {
                        toast.setText("数据格式不正确!");
                        toast.show();
                        this.et_result1.setText(value);
                        return;
                    }
                }
                toast.show();
                this.et_result1.setText(value);
            }
        }
    }

    public ListSearchResultsAdapter(Context context, List<Student> studentList, OnResponseProgramListener programListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.studentList = studentList;
        this.programListener = programListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultsHolder holder = new ResultsHolder(this.inflater.inflate(R.layout.adapter_list_results, parent, false));
        holder.setIsRecyclable(false);
        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ResultsHolder resultsHolder = (ResultsHolder) holder;
        Student student = (Student) this.studentList.get(position);
        resultsHolder.tv_stuInfo.setText(student.getName() + "(" + student.getStuNo() + ")");
        if (this.programListener.getProgram().getInputType() == 1) {
            resultsHolder.et_result1.setVisibility(View.VISIBLE);
            String[] units = this.programListener.getProgram().getUnit().split("/");
            resultsHolder.et_result.setHint(units[0]);
            resultsHolder.et_result1.setHint(units[1]);
            String[] values = student.getMark().split("/");
            if (values.length >= 2) {
                resultsHolder.et_result.setText(values[0]);
                resultsHolder.et_result1.setText(values[1]);
            }
        } else {
            resultsHolder.et_result1.setVisibility(View.GONE);
            if (this.programListener.getProgram().getMin() > 0 || this.programListener.getProgram().getMax() > 0) {
                resultsHolder.et_result.setHint(this.programListener.getProgram().getMin() + "~" + this.programListener.getProgram().getMax() + " " + this.programListener.getProgram().getUnit().split("\\(")[0]);
            } else {
                resultsHolder.et_result.setHint(this.programListener.getProgram().getUnit());
            }
            resultsHolder.et_result.setText(student.getMark().replace("'", "."));
        }
        if (student.getSex() == 2) {
            resultsHolder.tv_Sex.setText("女");
        } else if (student.getSex() == 1) {
            resultsHolder.tv_Sex.setText("男");
        } else {
            resultsHolder.tv_Sex.setText("男");
        }

        for (View view : viewList) {
            final Student stu = (Student) view.getTag();
            if (stu != null && stu.getId() == student.getId()) {
                viewList.remove(view);
                break;
            }
        }
        resultsHolder.itemView.setTag(R.string.tag, Integer.valueOf(position));
        resultsHolder.itemView.setTag(student);
        this.viewList.add(resultsHolder.itemView);
    }

    public int getItemCount() {
        return this.studentList == null ? 0 : this.studentList.size();
    }

    public void updata(List<Student> studentList) {
        this.viewList.clear();
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    public List<View> getViewList() {
        return this.viewList;
    }
}
