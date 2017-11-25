package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;

import java.util.Collection;
import java.util.List;

public class CategoryListView extends FrameLayout {

  public interface OnCategoryClickListener {
    void onCategoryClick(int categorySerial);
  }

  private OnCategoryClickListener onCategoryClickListener;

  private int selectedCategory;

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

  public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
    this.onCategoryClickListener = onCategoryClickListener;
  }

  /**
   * Doesn't update the view!
   */
  public void setSelectedCategory(int categorySerial) {
    this.selectedCategory = categorySerial;
  }


  public void setContent(Collection<CategoryDto> categoryTree) {
    TreeNode root = TreeNode.root();
    addNodesRecursively(root, categoryTree);

    AndroidTreeView treeView = new AndroidTreeView(getContext(), root);
    treeView.setDefaultViewHolder(ItemHolder.class);
    treeView.setDefaultAnimation(true);
    treeView.setDefaultContainerStyle(R.style.TreeNodeStyle);
    treeView.setUseAutoToggle(false);
    treeView.setDefaultNodeClickListener((node, value) -> {
      if (onCategoryClickListener != null) {
        onCategoryClickListener.onCategoryClick(((CategoryDto) value).getSerial());
      }
    });

    removeAllViews();
    addView(treeView.getView());
  }

  /**
   * Creates nodes for all categories of the current subtree. Each category's children become a new
   * subtree. If a category is selected, all of its parent nodes are expanded.
   *
   * @return whether the parent should be expanded
   */
  private boolean addNodesRecursively(TreeNode parent, Collection<CategoryDto> children) {
    boolean expandParent = false;
    for (CategoryDto child: children) {
      TreeNode node = new TreeNode(child);
      parent.addChild(node);
      boolean currentIsSelected = child.getSerial() == selectedCategory;
      if (currentIsSelected) {
        node.setSelected(true);
        expandParent = true;
      }
      boolean expandCurrent = addNodesRecursively(node, child.getChildren());
      if (expandCurrent) {
        node.setExpanded(true);
        expandParent = true;
      }
    }
    return expandParent;
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

      TextView name = view.findViewById(R.id.categoryItemName);
      name.setText(value.getName());
      if (node.isSelected()) {
        name.setBackgroundColor(0xFF7a9cff);
      }

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
