package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;

import java.util.List;

public class CategoryListView extends FrameLayout {

  public CategoryListView(Context context) {
    super(context);
    init();
  }

  public CategoryListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CategoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.view_category_list, this);
  }

  public void setContent(List<CategoryDto> categoryTree) {
    TreeNode root = TreeNode.root();
    addNodesRecursively(root, categoryTree);

    AndroidTreeView treeView = new AndroidTreeView(getContext(), root);
    treeView.setDefaultViewHolder(ItemHolder.class);
    treeView.setDefaultAnimation(true);
    treeView.setDefaultContainerStyle(R.style.TreeNodeStyle);
    treeView.setUseAutoToggle(false);
    treeView.setDefaultNodeClickListener((node, value) -> {
      Intent categoryIntent = new Intent(getContext(), CategoryActivity.class);
      categoryIntent.putExtra(CategoryActivity.CATEGORY_SERIAL, ((CategoryDto) value).getSerial());
      getContext().startActivity(categoryIntent);
    });

    removeAllViews();
    addView(treeView.getView());
  }

  private void addNodesRecursively(TreeNode parent, List<CategoryDto> categorySubTree) {
    for (CategoryDto category: categorySubTree) {
      TreeNode node = new TreeNode(category);
      parent.addChild(node);
      addNodesRecursively(node, category.getChildren());
    }
  }

  private static class ItemHolder extends TreeNode.BaseNodeViewHolder<CategoryDto> {

    private ImageButton expandBtn;

    public ItemHolder(Context context) {
      super(context);
    }

    @Override
    public View createNodeView(TreeNode node, CategoryDto value) {
      final LayoutInflater inflater = LayoutInflater.from(context);
      final View view = inflater.inflate(R.layout.item_category, null, false);

      ((TextView) view.findViewById(R.id.categoryItemName)).setText(value.getName());

      expandBtn = view.findViewById(R.id.categoryItemExpandBtn);
      if (node.isLeaf()) {
        expandBtn.setVisibility(View.INVISIBLE);
      } else {
        expandBtn.setOnClickListener(v -> getTreeView().toggleNode(node));
      }

      return view;
    }

    @Override
    public void toggle(boolean active) {
      expandBtn.setImageResource(active ? R.drawable.ic_expanded_item : R.drawable.ic_expand_item);
    }
  }
}
