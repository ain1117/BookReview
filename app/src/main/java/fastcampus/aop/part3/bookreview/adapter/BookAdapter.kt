package fastcampus.aop.part3.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fastcampus.aop.part3.bookreview.databinding.ItemBookBinding
import fastcampus.aop.part3.bookreview.model.Book


class BookAdapter(private val itemClickedListener: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class BookItemViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //ViewHolder:미리 만들어진 뷰

        fun bind(bookModel: Book) { //bookModel이라는 이름으로 Book 클래스를 가져온다.

            binding.titleTextView.text = bookModel.title //binding을 통해 item_book.xml에 접근한다.
            binding.descriptionTextView.text = bookModel.description
            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }


            Glide
                .with(binding.coverImageView.context) //컨택스트를 가져온다
                .load(bookModel.coverSmallUrl) //이미지 url주소를 가져온다
                .into(binding.coverImageView)//가져온 이미지를 어디에 넣을 것이냐

        }

    }//클래스 안에 있는 클래스 = inner

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookItemViewHolder {//미리 만들어진 뷰 홀더가 없을 경우 작성하는 함수
        return BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) //Viewgroup의 context를 전달한다.
    }

    override fun onBindViewHolder(
        holder: BookItemViewHolder,
        position: Int
    ) {//뷰홀더가 화면에 그려지게 되었을 때 데이터를 바인드하는 함수

        holder.bind(currentList[position]) //currentList에서 position안에 있는 정보를 꺼내어 bind함수에 전달한다.


        //리사이클러뷰가 실제로 뷰 포지션이 변경이 되었을 때 새로운 값을 할당할 지 말지 판단하여 결정한다.

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean { //old와 new아이템이 같은가

                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {

                return oldItem.id == newItem.id

            }

        }


    }

}